package is.fistlab.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Slf4j
public class ImportAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {
    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params){
        log.error("Validation failed: {}, trace: ", ex.getMessage());
    }
}
