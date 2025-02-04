package is.fistlab.exceptions.auth;

public class JwtTokenExpired extends RuntimeException {
    public JwtTokenExpired(final String message) {
        super(message);
    }
}
