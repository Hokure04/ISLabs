package is.fistlab.dto;

import is.fistlab.database.entities.Coordinates;

import lombok.*;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@ToString
@Builder
@AllArgsConstructor
public class StudyGroupDto {

    private Long id;

    private String name; //Поле не может быть null, Строка не может быть пустой

    private Coordinates coordinates; // Поле не может быть null

    //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private LocalDate creationDate = LocalDate.now();

    private long studentsCount; //Значение поля должно быть больше 0

    private long expelledStudents; //Значение поля должно быть больше 0

    private int transferredStudents; //Значение поля должно быть больше 0

    private String formOfEducation; //Поле может быть null

    private Long shouldBeExpelled; //Значение поля должно быть больше 0, Поле не может быть null

    private float averageMark; //Значение поля должно быть больше 0

    private String semesterEnum; //Поле может быть null

    private PersonDto groupAdmin; //Поле не может быть null

}

