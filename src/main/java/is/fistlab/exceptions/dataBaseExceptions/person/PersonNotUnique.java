package is.fistlab.exceptions.dataBaseExceptions.person;

public class PersonNotUnique extends RuntimeException {
    public PersonNotUnique(final String message) {
        super(message);
    }
}
