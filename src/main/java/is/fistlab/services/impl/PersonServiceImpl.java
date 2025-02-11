package is.fistlab.services.impl;

import is.fistlab.database.entities.Coordinates;
import is.fistlab.database.entities.Location;
import is.fistlab.database.entities.Person;
import is.fistlab.database.enums.Color;
import is.fistlab.database.enums.Country;
import is.fistlab.database.repositories.CoordinatesRepository;
import is.fistlab.database.repositories.LocationRepository;
import is.fistlab.database.repositories.PersonRepository;
import is.fistlab.database.repositories.specifications.PersonSpecifications;
import is.fistlab.exceptions.dataBaseExceptions.person.PersonNotExistException;
import is.fistlab.services.PersonService;
import is.fistlab.services.SpecialButtonsService;
import is.fistlab.utils.AuthenticationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class PersonServiceImpl implements PersonService, SpecialButtonsService {
    private final PersonRepository personRepository;
    private final AuthenticationUtils authenticationUtils;
    private final LocationRepository locationRepository;
    private final CoordinatesRepository coordinatesRepository;

    @Override
    @Transactional
    public void createPerson(final Person person) {
        var creator = authenticationUtils.getCurrentUserFromContext();
        person.setCreator(creator);
        personRepository.save(person);
        log.info("Created person: {}", person);
    }

    @Override
    @Transactional
    public void deletePersonById(final Long id) {
        Optional<Person> deletingPerson = personRepository.findById(id);

        if (deletingPerson.isEmpty()) {
            log.error("Person with id: {} does not exist", id);
            throw new PersonNotExistException("Пользователя с таким id не существует");
        }

        authenticationUtils.verifyAccess(deletingPerson.get());
        personRepository.deleteById(id);
        log.info("Deleted person: {}", id);
    }

    @Override
    public Page<Person> getAllPersons(final Pageable pageable) {
        return personRepository.findAll(pageable);
    }

    @Override
//    @Transactional
    public List<Person> addAll(List<Person> persons) {
        var savedList = personRepository.saveAll(persons);
        log.info("Saved {} persons", savedList.size());
        return savedList;
    }

    @Override
    @Transactional
    public Person updatePerson(final Person person) {
        if (!personRepository.existsById(person.getId())) {
            log.error("Person with id: {} does not exist, update is impossible", person.getId());
            throw new PersonNotExistException("Пользователь не найден");
        }

        Person personToUpdate = personRepository.getReferenceById(person.getId());
        authenticationUtils.verifyAccess(personToUpdate);

        var location = locationRepository.getReferenceById(personToUpdate.getId());
        Location locationFromPersonToUpdate = person.getLocation();
        locationFromPersonToUpdate.setId(location.getId());
        personToUpdate.setLocation(locationRepository.save(locationFromPersonToUpdate));

        var coordinates = coordinatesRepository.getReferenceById(personToUpdate.getCoordinates().getId());
        Coordinates coordinatesNew = person.getCoordinates();
        coordinatesNew.setId(coordinates.getId());
        personToUpdate.setCoordinates(coordinatesRepository.save(coordinatesNew));

        person.setCreator(personToUpdate.getCreator());
        var updatedPerson = personRepository.save(person);
        log.info("Updated person: {}", updatedPerson);

        return person;
    }


    @Override
    public Integer getHeightSum() {
        return personRepository.getSumHeight();
    }

    @Override
    public List<Person> getPersonsStartsWith(String prefix) {
        return personRepository.findByNameStartWith(prefix);
    }

    @Override
    public List<Double> getUniqueHeights() {
        return personRepository.getUniqueHeight();
    }

    @Override
    public Double getColorPercentage(Color color) {
        return personRepository.getPercent(color.name());
    }

    @Override
    public Integer getCountByHairColorByLocation(Color color, Location location) {
        return personRepository.getCountByColorAndLocation(color.name(), location.getX(),
                location.getY(), location.getZ());
    }

    @Override
    public List<Person> filterPersons(final String name, final Color eyeColor, final Color hairColor,
                                      final Integer height, final Country nationality,
                                      final LocalDate birthdayBefore) {
        Specification<Person> specification = Specification.where(null);

        if (Objects.nonNull(name)) {
            specification = specification.and(
                    PersonSpecifications.hasName(name));
        }
        if (Objects.nonNull(eyeColor)) {
            specification = specification.and(
                    PersonSpecifications.hasEyeColor(eyeColor));
        }
        if (Objects.nonNull(hairColor)) {
            specification = specification.and(
                    PersonSpecifications.hasHairColor(hairColor));
        }
        if (Objects.nonNull(height)) {
            specification = specification.and(
                    PersonSpecifications.hasHeightGreaterThan(height));
        }
        if (Objects.nonNull(nationality)) {
            specification = specification.and(
                    PersonSpecifications.hasNationality(nationality));
        }
        if (Objects.nonNull(birthdayBefore)) {
            specification = specification.and(
                    PersonSpecifications.hasBirthdayBefore(birthdayBefore));
        }
        return personRepository.findAll(specification);
    }

    @Override
    public Page<Person> getPagedResult(final List<Person> studyGroups, final Pageable pageable) {
        int start = Math.min((int) pageable.getOffset(), studyGroups.size());
        int end = Math.min((start + pageable.getPageSize()), studyGroups.size());
        return new PageImpl<>(studyGroups.subList(start, end), pageable, studyGroups.size());
    }

    @Override
    public Pageable getPageAfterSort(int page, int size, String sortBy, String sortDirection) {
        final List<String> allowedSortFields = List.of("id", "name", "nationality",
                "eyeColor", "hairColor",
                "height", "birthday");

        Sort sort = Sort.by("id"); // default

        if (Objects.nonNull(sortBy) && Objects.nonNull(sortDirection) && allowedSortFields.contains(sortBy)) {
            sort = Sort.by(sortBy);
            if ("desc".equalsIgnoreCase(sortDirection)) {
                sort = sort.descending();
            } else {
                sort = sort.ascending();
            }
        } else {
            log.warn("Invalid sortBy field: {}. Default sorting by id is used.", sortBy);
        }

        return PageRequest.of(page, size, sort);
    }
}