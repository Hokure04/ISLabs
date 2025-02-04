package is.fistlab.services;

import is.fistlab.database.entities.User;
import is.fistlab.dto.PersonDto;

import java.util.List;

public interface SeqServiceUserProcessing {
    void runSeq(List<PersonDto> personDtoList, User user);
}
