package is.fistlab.services;

import is.fistlab.database.entities.Person;
import is.fistlab.database.enums.Color;
import is.fistlab.database.enums.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public interface PersonService {
    Person updatePerson(Person person);

    void createPerson(Person person);

    void deletePersonById(Long id);

    Page<Person> getAllPersons(Pageable pageable);

    List<Person> filterPersons(final String name, final Color eyeColor, final Color hairColor,
                               final Integer height, final Country nationality,
                               final LocalDate birthdayBefore);

    Page<Person> getPagedResult(List<Person> personList, Pageable pageable);

    Pageable getPageAfterSort(int page, int size, String sortBy, String sortDirection);

}
