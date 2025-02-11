package is.fistlab.services.impl;

import is.fistlab.database.entities.User;
import is.fistlab.database.repositories.CoordinatesRepository;
import is.fistlab.database.repositories.LocationRepository;
import is.fistlab.database.repositories.PersonRepository;
import is.fistlab.dto.PersonDto;
import is.fistlab.exceptions.fileExceptions.FailedToReadFile;
import is.fistlab.services.ImportProcessing;
import is.fistlab.services.ImportService;
import is.fistlab.utils.parser.CSVParser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.Timestamp;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ImportServiceImpl implements ImportService {
    private final PersonRepository personRepository;
    private final LocationRepository locationRepository;
    private final CoordinatesRepository coordinatesRepository;
    private final ImportProcessing importProcessing;
    private static final Timestamp TIME_MARK = Timestamp.valueOf("2024-12-12 00:00:00");

    @Override
    public String importFile(MultipartFile file, User user, Timestamp userTimestamp) throws IOException {
        List<PersonDto> personDtoList = CSVParser.getPersonFromFile(getFile(file));
        String result;
        importProcessing.runImport(personDtoList, user, getFile(file));
        return "Удали меня";
    }


    @Transactional
    public void dropAll(){
        personRepository.deleteAll();
        locationRepository.deleteAll();
        coordinatesRepository.deleteAll();
    }

    private File getFile(MultipartFile multipartFile){
        try(InputStream inputStream = multipartFile.getInputStream()){
            File tempFile = File.createTempFile("uploaded-",".csv");
            tempFile.deleteOnExit();

            try(OutputStream outputStream = new FileOutputStream(tempFile)){
                inputStream.transferTo(outputStream);
            }catch (IOException e){
                throw new FailedToReadFile("Ошибка во время чтения файла");
            }
            return tempFile;
        } catch (IOException e){
            throw new FailedToReadFile("Ошибка во время чтения файла");
        }
    }
}
