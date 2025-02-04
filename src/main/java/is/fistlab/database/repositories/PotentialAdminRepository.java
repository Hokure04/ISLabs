package is.fistlab.database.repositories;

import is.fistlab.database.entities.PotentialAdmin;
import is.fistlab.database.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface PotentialAdminRepository extends JpaRepository<PotentialAdmin, Long> {
    void removeByUser(User user);

    @NonNull
    Page<PotentialAdmin> findAll(@NonNull Pageable pageable);
}
