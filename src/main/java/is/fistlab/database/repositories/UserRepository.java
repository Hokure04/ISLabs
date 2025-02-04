package is.fistlab.database.repositories;

import is.fistlab.database.entities.User;
import is.fistlab.database.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, java.lang.Long> {
    User findByUsername(String username);
    List<User> findAllByRole(UserRole userRole);
}
