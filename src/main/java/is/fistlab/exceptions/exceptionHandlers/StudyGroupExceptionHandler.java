package is.fistlab.exceptions.exceptionHandlers;

import is.fistlab.controllers.Response;
import is.fistlab.exceptions.dataBaseExceptions.studyGroup.StudyGroupAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class StudyGroupExceptionHandler {

    @ExceptionHandler(StudyGroupAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response<String> studyGroupAlreadyExistException(final StudyGroupAlreadyExistException e) {
        return new Response<>(e.getMessage());
    }
}
