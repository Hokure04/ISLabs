package is.fistlab.database.repositories.specifications;

import is.fistlab.database.entities.Person;
import is.fistlab.database.entities.User;
import is.fistlab.database.enums.Color;
import is.fistlab.database.enums.Country;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PersonSpecifications {

    public static Specification<Person> hasName(final String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Person> hasEyeColor(final Color eyeColor) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("eyeColor"), eyeColor);
    }

    public static Specification<Person> hasHairColor(final Color hairColor) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("hairColor"), hairColor);
    }

    public static Specification<Person> hasLocation(final String location) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("location").get("name"), "%" + location + "%");
    }


    public static Specification<Person> hasHeightGreaterThan(final int height) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThan(root.get("height"), height);
    }

    public static Specification<Person> hasNationality(final Country nationality) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("nationality"), nationality);
    }

    public static Specification<Person> createdAfter(final LocalDateTime date) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("creationDate"), date);
    }

    public static Specification<Person> hasBirthdayBefore(final LocalDate birthday) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThan(root.get("birthday"), birthday);
    }

}