package htwb.ai.authservice.service;

import htwb.ai.authservice.dto.UserRequest;
import htwb.ai.authservice.exception.AuthorizationException;
import htwb.ai.authservice.exception.InvalidAttributeValueException;
import htwb.ai.authservice.exception.ResourceNotFoundException;
import htwb.ai.authservice.model.User;
import htwb.ai.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static htwb.ai.authservice.util.JwtUtils.generateToken;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    @Value("${signing.key}")
    private String signingKey;

    public String authenticate(UserRequest userRequest)
            throws InvalidAttributeValueException, ResourceNotFoundException, AuthorizationException {
        if (userRequest.getUserId() == null || userRequest.getPassword() == null)
            throw new InvalidAttributeValueException("Missing attributes: Need an ID and password");

        Optional<User> userOptional = userRepository.findById(userRequest.getUserId());
        if (userOptional.isEmpty())
            throw new ResourceNotFoundException("User", "ID", userRequest.getUserId());

        User user = userOptional.get();

        if (! user.getPassword().equals(userRequest.getPassword()))
            throw new AuthorizationException("Wrong password");

        return generateToken(signingKey, user.getId(), 1000*60*60*24);
    }
}
