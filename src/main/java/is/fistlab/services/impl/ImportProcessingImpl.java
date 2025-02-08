package is.fistlab.services.impl;

import is.fistlab.database.entities.*;
import is.fistlab.dto.PersonDto;
import is.fistlab.mappers.PersonMapper;
import is.fistlab.services.*;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Override
    public void runImport(List<PersonDto> personDtoList, User user){
        List<Person> personList = new ArrayList<>(personDtoList.size());
        List<Location> locationList = new ArrayList<>(personDtoList.size());
        List<Coordinates> coordinatesList = new ArrayList<>(personDtoList.size());
        var operation = new Operation();
        operation.setUser(user);
        operation.setIsFinished(false);

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

            operation.setIsFinished(true);
            operation.setAmountOfObjectSaved(listSavedPerson.size());
            operationService.add(operation);
        }catch (RuntimeException e){
            operation.setIsFinished(false);
            operationService.add(operation);
            throw e;
        }
    }

    public enum ImportMode{
        SEQUENTIAL,
        ASYNC
    }
}
