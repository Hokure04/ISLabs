package is.fistlab.controllers;

import is.fistlab.database.enums.UserRole;
import is.fistlab.dto.JwtAuthenticationResponse;
import is.fistlab.dto.UserDto;
import is.fistlab.security.sevices.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthorizationController {

    private final AuthService authenticationService;

    @PostMapping("/create-user")
    public ResponseEntity<Response<JwtAuthenticationResponse>> registration(@RequestBody final UserDto dto) {
        return ResponseEntity.ok(new Response<>(authenticationService.signUp(dto)));
    }

    @GetMapping("/is-token-valid")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Response<String> isTokenValid() {
        return new Response<>("Токен валиден", authenticationService.getUserRole().toString());
    }

    @GetMapping("/get-role")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public Response<UserRole> getUserRole() {
        return new Response<>(authenticationService.getUserRole());
    }

    @GetMapping("/get-username")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Response<String>> getUsername() {
        return ResponseEntity.ok(
                new Response<>(null, authenticationService.getUsername())
        );
    }

    @PostMapping("/login")
    public Response<JwtAuthenticationResponse> login(@RequestBody final UserDto dto) {
        return new Response<>(authenticationService.signIn(dto));
    }

}
