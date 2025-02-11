package is.fistlab.controllers;

import is.fistlab.database.entities.Person;
import is.fistlab.database.enums.Color;
import is.fistlab.database.enums.Country;
import is.fistlab.dto.LocationDto;
import is.fistlab.dto.PersonDto;
import is.fistlab.mappers.LocationMapper;
import is.fistlab.mappers.PersonMapper;
import is.fistlab.services.PersonService;
import is.fistlab.services.SpecialButtonsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/manage/persons")
@Slf4j
@CrossOrigin
@AllArgsConstructor
public class PersonController {
    private final PersonService personService;
    private final SpecialButtonsService specialButtonsService;

    @GetMapping("/persons-names")
    public ResponseEntity<Response<Page<Person>>> getAllPersonsName(
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int size,
            @RequestParam(required = false) final String sortBy,
            @RequestParam(required = false) final String sortDirection) {

        var pageable = personService.getPageAfterSort(
                page,
                size,
                sortBy,
                sortDirection);

        Page<Person> personPage = personService.getAllPersons(pageable);
        return ResponseEntity.ok(new Response<>(personPage));
    }


    @PostMapping("/create-person")
    public ResponseEntity<Response<Person>> createPerson(@RequestBody final PersonDto dto) {

        Person person = PersonMapper.toEntity(dto);
        personService.createPerson(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new Response<>("Пользователь успешно создан", person)
        );
    }

    @PostMapping("/update-person")
    public ResponseEntity<Response<Person>> updatePerson(@RequestBody final PersonDto dto) {
        Person person = PersonMapper.toEntity(dto);
        var updatedPerson = personService.updatePerson(person);
        return ResponseEntity.ok(new Response<>("Данные о человеке: " + dto.getName() + " успешно обновлены", updatedPerson));
    }

    @DeleteMapping("/delete-person-by-id/{id}")
    public ResponseEntity<Response<String>> deletePersonById(@PathVariable final Long id) {
        personService.deletePersonById(id);
        return ResponseEntity.ok(new Response<>("Пользователь с id: " + id + " успешно удален"));
    }


    @GetMapping("/height-sum")
    public Integer getAllPersonsHeightSum() {
        return specialButtonsService.getHeightSum();
    }

    @GetMapping("/start/{prefix}")
    public List<Person> startPerson(@PathVariable final String prefix) {
        return specialButtonsService.getPersonsStartsWith(prefix);
    }

    @GetMapping("/unique-heights")
    public ResponseEntity<List<Double>> getUniqueHeights() {
        return ResponseEntity.ok(specialButtonsService.getUniqueHeights());
    }

    @GetMapping("/get-percent/{color}")
    public Double getColorPercentage(@PathVariable final String color) {
        try {
            return specialButtonsService.getColorPercentage(Color.valueOf(color));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Неверный цвет");
        }
    }

    @GetMapping("/count-by-hair-color/{color}/{x}/{y}/{z}")
    public ResponseEntity<Integer> getCountByColorAndLocation(@PathVariable final String color,
                                                              @PathVariable final Integer x,
                                                              @PathVariable final Long y,
                                                              @PathVariable final Float z) {
        try {
            return ResponseEntity.ok(specialButtonsService.getCountByHairColorByLocation(Color.valueOf(color),
                    LocationMapper.toEntity(
                            LocationDto.builder()
                                    .x(x)
                                    .y(y)
                                    .z(z)
                                    .build())
            ));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Неверный цвет");
        }
    }


    @GetMapping("/filter")
    public ResponseEntity<Response<Page<Person>>> getFilteredStudyGroups(
            @RequestParam(required = false) final String name,
            @RequestParam(required = false) final Color eyeColor,
            @RequestParam(required = false) final Color hairColor,
            @RequestParam(required = false) final Integer height,
            @RequestParam(required = false) final Country nationality,
            @RequestParam(required = false) final LocalDate birthdayBefore,
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "10") final int size) {

        Pageable pageable = PageRequest.of(page, size);
        List<Person> studyGroups = personService.filterPersons(
                name, eyeColor,
                hairColor, height,
                nationality, birthdayBefore
        );

        Page<Person> studyGroupPage = personService.getPagedResult(studyGroups, pageable);

        return ResponseEntity.ok(new Response<>(studyGroupPage));
    }


}
