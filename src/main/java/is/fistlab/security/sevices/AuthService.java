package is.fistlab.security.sevices;

import is.fistlab.database.entities.User;
import is.fistlab.database.enums.UserRole;
import is.fistlab.dto.JwtAuthenticationResponse;
import is.fistlab.dto.UserDto;

public interface AuthService {
    JwtAuthenticationResponse signUp(UserDto requestDto);
    JwtAuthenticationResponse signIn(UserDto request);
    UserRole getUserRole();
    String getUsername();
    User getCurrentUser();
}
