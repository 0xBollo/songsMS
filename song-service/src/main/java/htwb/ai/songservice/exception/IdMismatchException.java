package htwb.ai.songservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IdMismatchException extends RuntimeException {

    public IdMismatchException(String msg) {
        super(msg);
    }
}
