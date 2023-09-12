package htwb.ai.songservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenResourceAccessException extends RuntimeException {

    public ForbiddenResourceAccessException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("Access to %s with %s = %s not allowed", resourceName, fieldName, fieldValue));
    }
}
