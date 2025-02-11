package is.fistlab.controllers;

import io.minio.errors.MinioException;
import is.fistlab.database.entities.Operation;
import is.fistlab.security.sevices.AuthService;
import is.fistlab.services.ImportService;
import is.fistlab.services.MinioService;
import is.fistlab.services.OperationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/v1/import")
@Slf4j
@AllArgsConstructor
public class ImportController {
    private final ImportService importService;
    private final AuthService authService;
    private final OperationService operationService;
    private final MinioService minioService;

    @PostMapping("/csv")
    public ResponseEntity<Response<Integer>> importPerson(@RequestParam("file") final MultipartFile file, @RequestParam("userTimestamp") final Timestamp userTimestamp) throws IOException{
        var currentUser = authService.getCurrentUser();
        log.info("User {} started import", currentUser);
        var result = importService.importFile(file, currentUser, userTimestamp);
        log.info("finished import");
        return ResponseEntity.ok(new Response<>("Сохранение начали "+result));
    }

    @PostMapping("/drop")
    public void dropAll(){
        importService.dropAll();
    }

    @GetMapping("/history")
    public ResponseEntity<List<Operation>> getHistory(){
        var currentUser = authService.getCurrentUser();
        var operations = operationService.getOperations(currentUser);
        return ResponseEntity.ok(operations);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("filename") String filename){
        try{
            InputStream fileStream = minioService.downloadFile(filename);
            byte[] fileContent = fileStream.readAllBytes();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());
            return ResponseEntity.ok().headers(headers).body(fileContent);
        }catch (MinioException | IOException e){
            log.error("Оибка при скачивании файла {}", filename, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
