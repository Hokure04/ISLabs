package is.fistlab.mappers;

import is.fistlab.database.entities.Location;
import is.fistlab.dto.LocationDto;

import java.util.Objects;

public class LocationMapper {
    public static Location toEntity(LocationDto dto){
        Location location = new Location();
        if(Objects.nonNull(dto.getX())){
            location.setX(dto.getX());
        }
        if(Objects.nonNull(dto.getY())){
            location.setY(dto.getY());
        }
        if(Objects.nonNull(dto.getZ())){
            location.setZ(dto.getZ());
        }

        return location;
    }
}
