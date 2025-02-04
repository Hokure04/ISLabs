package is.fistlab.exceptions.exceptionHandlers;

import is.fistlab.controllers.Response;
import is.fistlab.exceptions.mappers.InvalidFieldException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MappersExceptionHandler {

    @ExceptionHandler(InvalidFieldException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<String> fieldInvalid(final InvalidFieldException exc) {
        return new Response<>(exc.getMessage());
    }

}
