package is.fistlab.database.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Data
@NoArgsConstructor
public class Coordinates {
    @Id
    @GeneratedValue
    private Long id;
    @Min(-646)
    @Column(nullable = false)
    private Integer x;
    private Float y;
}
