package is.fistlab.database.entities;

import is.fistlab.services.impl.ImportProcessingImpl;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "app_user")
    private User user;

    @Column(name="amount_of_object_saved", nullable = true)
    private int amountOfObjectSaved;

    @Column(name = "is_finished")
    private Boolean isFinished;

}
