package is.fistlab.services;

import is.fistlab.database.entities.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;

public interface ImportService {
    String importFile(MultipartFile file, User user, Timestamp timestamp) throws IOException;
    void dropAll();
}
