package is.fistlab.security.sevices.Impl;

import is.fistlab.database.entities.User;
import is.fistlab.database.enums.UserRole;
import is.fistlab.dto.JwtAuthenticationResponse;
import is.fistlab.dto.UserDto;
import is.fistlab.exceptions.auth.UserAlreadyExist;
import is.fistlab.security.sevices.AuthService;
import is.fistlab.security.sevices.JwtService;
import is.fistlab.services.AdminProcessingService;
import is.fistlab.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AdminProcessingService adminProcessingService;

    public JwtAuthenticationResponse signUp(final UserDto requestDto) {

        var user = User.builder()
                .username(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .role(UserRole.ROLE_USER)
                .build();

        if (userService.isUserExists(user.getUsername())) {
            throw new UserAlreadyExist("Пользователь с таким ником уже существует");
        }

        var savedUser = userService.createNewUser(user);

        if (UserRole.valueOf(requestDto.getRole()) == UserRole.ROLE_ADMIN) {
            savedUser = setAdminOrAddToWaitList(savedUser);
        }

        var jwt = jwtService.generateToken(savedUser);
        return new JwtAuthenticationResponse(jwt);
    }

    private User setAdminOrAddToWaitList(final User userSavedInRepository) {
        var toReturn = userSavedInRepository;
        if (!adminProcessingService.isAnyAdminExist()) {
            userSavedInRepository.setRole(UserRole.ROLE_ADMIN);
            toReturn = userService.updateUser(userSavedInRepository);
        } else {
            adminProcessingService.addUserToWaitingList(userSavedInRepository);
        }
        return toReturn;
    }


    public JwtAuthenticationResponse signIn(final UserDto request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService
                .getUserDetailsService()
                .loadUserByUsername(request.getUsername());

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    @Override
    public UserRole getUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            User currentUser = (User) principal;

            return currentUser.getRole();
        }
        return null;
    }

    @Override
    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            User currentUser = (User) principal;

            return currentUser.getUsername();
        }
        return null;
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        }
        return null;
    }
}
