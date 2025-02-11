package is.fistlab.services.impl;

import is.fistlab.database.entities.Location;
import is.fistlab.database.repositories.LocationRepository;
import is.fistlab.services.LocationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    @Override
    @Transactional
    public Location add(Location location){
        var result = locationRepository.save(location);
        log.info("Created location: {}", result);
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public List<Location> addAll(List<Location> locations){
        var result = locationRepository.saveAll(locations);
        log.info("Created {} locations", result.size());
        return result;
    }
}
