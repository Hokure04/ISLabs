package is.fistlab.exceptions.auth;

public class UserAlreadyExist extends RuntimeException {
    public UserAlreadyExist(final String message) {
        super(message);
    }
}
