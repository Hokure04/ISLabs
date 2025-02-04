package is.fistlab.mappers;

import is.fistlab.database.entities.User;
import is.fistlab.database.enums.UserRole;
import is.fistlab.dto.UserDto;
import is.fistlab.exceptions.mappers.InvalidFieldException;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;


@Slf4j
public class UserMapper {

    public static User toEntity(final UserDto dto) {
        var userBuilder = User.builder();

        if (Objects.isNull(dto.getUsername())) {
            log.warn("Username is null");
            throw new InvalidFieldException("Username is required");
        }
        if (Objects.isNull(dto.getPassword())) {
            log.warn("Password is null");
            throw new InvalidFieldException("Password is required");
        }
        if (Objects.isNull(dto.getRole())) {
            log.warn("Role is null");
            throw new InvalidFieldException("Role is required");
        }
        UserRole role;
        try {
            role = UserRole.valueOf(dto.getRole());
        } catch (IllegalArgumentException e) {
            log.warn("Invalid role");
            throw new InvalidFieldException("Invalid role");
        }

        userBuilder
                .username(dto.getUsername())
                .password(dto.getPassword())
                .role(role);

        return userBuilder.build();
    }
}
