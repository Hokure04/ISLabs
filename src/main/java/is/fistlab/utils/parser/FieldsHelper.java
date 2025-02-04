package is.fistlab.utils.parser;

import lombok.Getter;

@Getter
public enum FieldsHelper {
    PERSON_NAME(0),
    PERSON_EYE_COLOR(1),
    PERSON_HAIR_COLOR(2),
    PERSON_LOCATION_X(3),
    PERSON_LOCATION_Y(4),
    PERSON_LOCATION_Z(5),
    COORDINATE_X(6),
    COORDINATE_Y(7),
    PERSON_HEIGHT(8),
    PERSON_NATIONALITY(9),
    BIRTHDAY(10);

    private final int index;

    FieldsHelper(int index){
        this.index = index;
    }
}
