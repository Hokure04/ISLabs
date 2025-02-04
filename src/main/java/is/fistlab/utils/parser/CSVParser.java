package is.fistlab.utils.parser;

import is.fistlab.database.entities.Coordinates;
import is.fistlab.database.entities.Location;
import is.fistlab.dto.PersonDto;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.util.ArrayList;

import static is.fistlab.utils.parser.FieldsHelper.*;

public class CSVParser {
    private static final String[] HEADERS = {"personName","personEyeColor", "personHairColor",
            "personLocationX", "personLocationY","personLocationZ","coordinateX","coordinateY",
            "personHeight","personNationality","birthday"};

    public static ArrayList<PersonDto> getPersonFromFile(final File file){
        final ArrayList<PersonDto> list = new ArrayList<>();
        try(Reader in = new FileReader(file)){
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader(HEADERS)
                    .setSkipHeaderRecord(true).build();

            final Iterable<CSVRecord> records = csvFormat.parse(in);

            for(CSVRecord record : records){
                var person = parsePerson(record);
                list.add(person);
            }

            return list;
        }catch (IOException e){
         throw new RuntimeException(e);
        }
    }

    private static PersonDto parsePerson(CSVRecord record){
        var coordinates = parseCoordinates(record);
        var location = parseLocation(record);
        PersonDto personDto = new PersonDto();

        personDto.setName(
                getRecordValue(record, PERSON_NAME.getIndex())
        );
        personDto.setEyeColor(
                getRecordValue(record, PERSON_EYE_COLOR.getIndex())
        );
        personDto.setHairColor(
                getRecordValue(record, PERSON_HAIR_COLOR.getIndex())
        );
        personDto.setLocation(location);
        personDto.setCoordinates(coordinates);

        personDto.setHeight(
                Integer.parseInt(getRecordValue(record, PERSON_HEIGHT.getIndex()))
        );
        personDto.setNationality(
                getRecordValue(record, PERSON_NATIONALITY.getIndex())
        );
        personDto.setBirthday(
                LocalDate.parse(getRecordValue(record, BIRTHDAY.getIndex()))
        );

        return personDto;
    }

    private static Coordinates parseCoordinates(CSVRecord record){
        var coordinates = new Coordinates();
        coordinates.setX(
                Integer.parseInt(
                        getRecordValue(record, COORDINATE_X.getIndex())
                )
        );
        var value = getRecordValue(record, COORDINATE_Y.getIndex());
        coordinates.setY(
                Float.parseFloat(
                        getRecordValue(record, COORDINATE_Y.getIndex())
                )
        );
        return coordinates;
    }

    private static Location parseLocation(CSVRecord record){
        var personLocation = new Location();
        personLocation.setX(
                Integer.parseInt(
                        getRecordValue(record, PERSON_LOCATION_X.getIndex())
                )
        );

        personLocation.setY(
                Long.valueOf(
                        getRecordValue(record, PERSON_LOCATION_Y.getIndex())
                )
        );

        personLocation.setZ(
                Float.parseFloat(
                        getRecordValue(record, PERSON_LOCATION_Z.getIndex())
                )
        );

        return personLocation;
    }

    private static String getRecordValue(CSVRecord record, int index){
        return record.get(index).trim();
    }
}
