package is.fistlab.services;

import is.fistlab.database.entities.Coordinates;

import java.util.List;

public interface CoordinateService {
    List<Coordinates> addAll(List<Coordinates> coordinatesList);
}
