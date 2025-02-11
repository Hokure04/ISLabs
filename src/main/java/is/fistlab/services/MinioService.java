package is.fistlab.services;

import io.minio.errors.MinioException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface MinioService {
    String uploadFile(String username, final File byteContent) throws IOException, MinioException;
    InputStream downloadFile(String filename) throws MinioException, IOException;
    void deleteFile(final String username, final String filename);
}
