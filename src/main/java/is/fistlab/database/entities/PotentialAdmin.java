package is.fistlab.database.entities;

import is.fistlab.database.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;

/**
 * Сущность, которая будет хранить очередь на становление админом
 */
@Entity
@Data

public class PotentialAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @Enumerated(EnumType.STRING)
    private UserRole wantedRole;
}

