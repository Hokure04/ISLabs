package is.fistlab.exceptions.dataBaseExceptions.studyGroup;

public class StudyGroupAlreadyExistException extends RuntimeException {
    public StudyGroupAlreadyExistException(final String message) {
        super(message);
    }
}
