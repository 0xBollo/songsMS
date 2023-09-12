package htwb.ai.songservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Is thrown if an object has already been assigned an ID before a valid ID could be computed
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ForbiddenIdAssignmentException extends RuntimeException {

    public ForbiddenIdAssignmentException(String msg) {
        super(msg);
    }
}