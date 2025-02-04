package is.fistlab.services;

import is.fistlab.database.entities.User;
import is.fistlab.dto.PersonDto;

import java.util.List;

public interface AsyncServiceUserProcessing {
    void runAsync(List<PersonDto> personDtoList, User user);
}
