package is.fistlab.services.impl;

import is.fistlab.database.entities.User;
import is.fistlab.database.repositories.UserRepository;
import is.fistlab.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public User createNewUser(final User user) {
        user.setPassword(user.getPassword());

        User newUser = userRepository.save(user);
        log.info("{} registered successfully", user.getUsername());

        return newUser;
    }

    @Transactional
    @Override
    public User updateUser(final User user) {
        userRepository.getReferenceById(user.getId());
        User newUser = userRepository.save(user);
        log.info("{} updated successfully", user.getUsername());
        return newUser;
    }


    @Override
    public boolean isUserExists(final String username) {
        User user = userRepository.findByUsername(username);
        if (Objects.nonNull(user)) {
            log.info("User with username: {} exist", username);
            return true;
        }
        log.info("User with username: {} not exist", username);
        return false;
    }

    @Override
    public User getUserByUsername(final String username) {
        if (!isUserExists(username)) {
            throw new UsernameNotFoundException("User with username: " + username + " not found");
        }
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetailsService getUserDetailsService() {
        return this::getUserByUsername;
    }

}
