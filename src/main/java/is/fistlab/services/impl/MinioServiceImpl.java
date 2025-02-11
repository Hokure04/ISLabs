package is.fistlab.services.impl;

import io.minio.*;
import io.minio.errors.MinioException;
import is.fistlab.services.MinioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@Slf4j
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {

    @Value("${minio.url}")
    private String minioUrl;

    @Value("${minio.bucket}")
    private String bucketName;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    private final MinioClient minioClient;

    @Override
    public String uploadFile(final String username, final File file) throws IOException, MinioException{
        try{
            BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket(bucketName).build();
            boolean bucketExists = minioClient.bucketExists(bucketExistsArgs);
            if(!bucketExists){
                MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder().bucket(bucketName).build();
                minioClient.makeBucket(makeBucketArgs);
            }
        } catch (MinioException e){
            log.error("Error checking if bucket exists or creating bucket", e);
            throw new MinioException("ERROR: checking bucket or creating bucket");
        } catch (IOException e){
            throw new RuntimeException("IO error occurred while checking if bucket exists", e);
        } catch (InvalidKeyException e){
            throw new RuntimeException("Invalid key error occurred while checking if bucket exists", e);
        } catch (NoSuchAlgorithmException e){
            throw new RuntimeException("No such algorithm error occurred while checking if bucket exists", e);
        }

        String filename = file.getName();
        try(InputStream inputStream = new FileInputStream(file)) {

            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filename)
                    .stream(inputStream, file.length(), -1)
                    .contentType("application/octet-stream")
                    .build();

            minioClient.putObject(putObjectArgs);
            return filename;
        } catch (MinioException e){
            log.info("Minio error occurred while uploading file {}", filename, e);
            throw new RuntimeException("Minio error occurred while uploading file", e);
        } catch (IOException e){
            throw new RuntimeException("IO error occurred while uploading file", e);
        }catch (InvalidKeyException e){
            throw new RuntimeException("Invalid key error occurred while uploading file", e);
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException("No such algorithm error occurred while uploading file", e);
        }
    }

    @Override
    public InputStream downloadFile(String filename) throws MinioException, IOException {
        try{
            GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filename)
                    .build();

            return minioClient.getObject(getObjectArgs);
        } catch (MinioException e){
            log.info("Minio error occurred while uploading file {}", filename, e);
            throw new RuntimeException("Minio error occurred while uploading file", e);
        } catch (IOException e){
            throw new RuntimeException("IO error occurred while uploading file", e);
        }catch (InvalidKeyException e){
            throw new RuntimeException("Invalid key error occurred while uploading file", e);
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException("No such algorithm error occurred while uploading file", e);
        }
    }

    @Override
    public void deleteFile(final String username, final String filename){
        String filenameForStorage = getNewFileName(username, filename);
        try{
            RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filenameForStorage)
                    .build();
            minioClient.removeObject(removeObjectArgs);
        }catch (MinioException e){
            log.info("Minio error occurred while uploading file {}", filename, e);
            throw new RuntimeException("Minio error occurred while uploading file", e);
        } catch (IOException e){
            throw new RuntimeException("IO error occurred while uploading file", e);
        }catch (InvalidKeyException e){
            throw new RuntimeException("Invalid key error occurred while uploading file", e);
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException("No such algorithm error occurred while uploading file", e);
        }
    }

    private String getNewFileName(String username, String filename){
        return username.trim() + "/" +filename.trim();
    }
}
