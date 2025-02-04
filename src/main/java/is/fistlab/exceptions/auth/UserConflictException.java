package is.fistlab.exceptions.auth;

public class UserConflictException extends RuntimeException {
    public UserConflictException(final String message) {
        super(message);
    }
}
