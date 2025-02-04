package is.fistlab.dto;

import is.fistlab.database.entities.Coordinates;
import is.fistlab.database.entities.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {
    private Long id;

    private String name; //Поле не может быть null, Строка не может быть пустой

    private String eyeColor; //Поле может быть null

    private String hairColor; //Поле может быть null

    private Coordinates coordinates; //Поле не может быть null

    private Location location; //Поле не может быть null

    private int height; //Значение поля должно быть больше 0

    private String nationality;

    private LocalDate birthday;

    private UserDto creatorDto;
}
