package htwb.ai.authservice.controller;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;

import htwb.ai.authservice.dto.UserRequest;
import htwb.ai.authservice.exception.AuthorizationException;
import htwb.ai.authservice.exception.InvalidAttributeValueException;
import htwb.ai.authservice.exception.ResourceNotFoundException;
import htwb.ai.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController @RequestMapping(path = "rest/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<String> authenticate(@RequestBody UserRequest userRequest)
            throws InvalidAttributeValueException, ResourceNotFoundException, AuthorizationException {
        String token = authService.authenticate(userRequest); // throws Exceptions
        return ResponseEntity.status(OK).contentType(TEXT_PLAIN).body(token);
    }
}
