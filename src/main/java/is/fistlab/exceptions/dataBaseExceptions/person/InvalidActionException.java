package is.fistlab.exceptions.dataBaseExceptions.person;

public class InvalidActionException extends RuntimeException {
    public InvalidActionException(final String message) {
        super(message);
    }
}
