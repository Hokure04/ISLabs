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
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
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
            var listSavedPerson = personService.addAll(personList);

            fileNameForMinio = minioService.uploadFile(user.getUsername(), file);
            operation.setIsFinished(true);
            operation.setAmountOfObjectSaved(listSavedPerson.size());
            operationService.add(operation);
        }catch (RuntimeException e){
            log.error("Error during import operation, rolling back", e);

            if(fileNameForMinio != null){
                try{
                    minioService.deleteFile(user.getUsername(), file.getName());
                    log.info("File removed from Minio due to failure {}", fileNameForMinio);
                }catch (Exception ex){
                    log.error("Error removing file file from Minio {}", fileNameForMinio, ex);
                }
            }
            operation.setIsFinished(false);
            operationService.add(operation);
            throw new RuntimeException("Operation filed, all changes rolled back", e);
        } catch (MinioException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public enum ImportMode{
        SEQUENTIAL,
        ASYNC
    }
}
