package is.fistlab.services;

import is.fistlab.database.entities.Location;

import java.util.List;

public interface LocationService {
    Location add(Location location);
    List<Location> addAll(List<Location> locations);
}
