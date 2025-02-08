package is.fistlab.database.repositories;

import is.fistlab.database.entities.Operation;
import is.fistlab.database.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
    List<Operation> findByUser(User user);
    List<Operation> findAll();
}
