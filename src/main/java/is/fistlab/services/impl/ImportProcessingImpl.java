package is.fistlab.services.impl;

import io.minio.errors.MinioException;
import is.fistlab.database.entities.*;
import is.fistlab.dto.PersonDto;
import is.fistlab.mappers.PersonMapper;
import is.fistlab.services.*;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@Transactional(propagation = Propagation.REQUIRED)
@AllArgsConstructor
public class ImportProcessingImpl implements ImportProcessing {
    private final PersonService personService;
    private final EntityManager entityManager;
    private final LocationService locationService;
    private final CoordinateService coordinateService;
    private final OperationService operationService;
    private final MinioService minioService;

    @Transactional
    @Override
    public void runImport(List<PersonDto> personDtoList, User user, File file){
        List<Person> personList = new ArrayList<>(personDtoList.size());
        List<Location> locationList = new ArrayList<>(personDtoList.size());
        List<Coordinates> coordinatesList = new ArrayList<>(personDtoList.size());
        Operation operation = new Operation();
        operation.setUser(user);
        operation.setIsFinished(false);

        String fileNameForMinio = null;
        try{
            for(var personDto : personDtoList){
                var person = PersonMapper.toEntity(personDto);
                person.setCreator(user);

                locationList.add(person.getLocation());
                coordinatesList.add(person.getCoordinates());
                personList.add(person);
            }

            locationService.addAll(locationList);
            entityManager.flush();

            fileNameForMinio = minioService.uploadFile(user.getUsername(), file);
            operation.setFilename(file.getName());
            /*try{
                fileNameForMinio = minioService.uploadFile(user.getUsername(), file);
                operation.setFilename(file.getName());
            }catch (Exception e){
                log.warn("Minio unavailable, skipping file upload: {}", e.getMessage());
            }*/

            prepareCommit(personList, operation);
        }catch (Exception e){
            log.error("Error during import operation, rolling back", e);
            rollback(fileNameForMinio, user, file.getName());
            throw new RuntimeException("Ошибка импорта, откатываем изменения", e);
        }
    }

    private void prepareCommit(List<Person> personList, Operation operation){
        personService.addAll(personList);
        operation.setIsFinished(true);
        operation.setAmountOfObjectSaved(personList.size());
        operationService.add(operation);
    }

    private void rollback(String fileNameForMinio, User user, String originalFileName){
        if(fileNameForMinio != null){
            try{
                minioService.deleteFile(user.getUsername(), originalFileName);
                log.info("Файл {} удален из Minio из-за ошибки", fileNameForMinio);
            }catch (Exception ex){
                log.error("Ошибка при удалении файла из Minio {}", fileNameForMinio, ex);
            }
        }
    }

    public enum ImportMode{
        SEQUENTIAL,
        ASYNC
    }
}
