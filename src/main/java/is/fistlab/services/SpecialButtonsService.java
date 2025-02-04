package is.fistlab.services;

import is.fistlab.database.entities.Location;
import is.fistlab.database.entities.Person;
import is.fistlab.database.enums.Color;

import java.util.List;

public interface SpecialButtonsService {

    Integer getHeightSum();
    List<Person> getPersonsStartsWith(String prefix);
    List<Double> getUniqueHeights();
    Double getColorPercentage(Color color);
    Integer getCountByHairColorByLocation(Color color, Location location);
}
