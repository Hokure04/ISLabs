package is.fistlab.exceptions.fileExceptions;

public class FailedToReadFile extends RuntimeException {
    public FailedToReadFile(String message){
        super(message);
    }
}
