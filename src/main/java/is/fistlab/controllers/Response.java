package is.fistlab.controllers;

import lombok.Getter;
import lombok.Setter;

/**
 * Обертка для того, чтобы можно было удобно укладывать уведомление и основную информацию
 */

@Getter
@Setter
public class Response<T> {

    private final String message;
    private final Object body;

    public Response(final String message, final T body) {
        this.message = message;
        this.body = body;
    }

    public Response(final String message) {
        this(message, null);
    }

    public Response(final T body) {
        this(null, body);
    }
}
