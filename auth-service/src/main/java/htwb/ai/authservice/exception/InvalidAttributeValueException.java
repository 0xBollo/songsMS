package htwb.ai.authservice.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Is thrown when an attribute has an invalid value for further processing
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidAttributeValueException extends RuntimeException {

    public InvalidAttributeValueException(String msg) {
        super(msg);
    }
}