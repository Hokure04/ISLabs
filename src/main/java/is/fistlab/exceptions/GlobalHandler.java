package is.fistlab.exceptions;

import is.fistlab.controllers.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response<String> methodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        log.error(e.getLocalizedMessage());
        return new Response<>("Невалидное значение фильтра", null);
    }
}
