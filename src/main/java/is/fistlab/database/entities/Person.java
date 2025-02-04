package is.fistlab.database.entities;

import is.fistlab.database.enums.Color;
import is.fistlab.database.enums.Country;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Validated
@Builder
@AllArgsConstructor
public class Person implements CreatorAware {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 1)
    private String name; //Поле не может быть null, Строка не может быть пустой

    @Enumerated(EnumType.STRING)
    private Color eyeColor; //Поле может быть null

    @Enumerated(EnumType.STRING)
    private Color hairColor; //Поле может быть null

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false, referencedColumnName = "id")
    private Location location; //Поле не может быть null

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false, referencedColumnName = "id")
    private Coordinates coordinates; //Поле не может быть null

    @Min(1)
    private int height; //Значение поля должно быть больше 0

    @Column
    @Enumerated(EnumType.STRING)
    private Country nationality; //Поле не может быть null

    private LocalDateTime creationDate;

    @Column(nullable = false)
    private LocalDate birthday;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private User creator;

    @PrePersist
    public void prePersist(){
        creationDate = LocalDateTime.now();
    }
}
