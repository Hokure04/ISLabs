package is.fistlab.exceptions.dataBaseExceptions.person;

public class PersonNotExistException extends RuntimeException {
    public PersonNotExistException(final String message) {
        super(message);
    }
}
