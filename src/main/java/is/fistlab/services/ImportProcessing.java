package is.fistlab.services;

import io.minio.errors.MinioException;
import is.fistlab.database.entities.User;
import is.fistlab.dto.PersonDto;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ImportProcessing {
    void runImport(List<PersonDto> personDtoList, User user, File file);
}
