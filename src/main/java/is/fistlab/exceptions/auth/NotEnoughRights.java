package is.fistlab.exceptions.auth;

public class NotEnoughRights extends RuntimeException {
    public NotEnoughRights(final String message) {
        super(message);
    }
}
