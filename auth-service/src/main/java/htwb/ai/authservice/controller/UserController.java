package htwb.ai.authservice.controller;

import htwb.ai.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController @RequestMapping(path = "rest/users")
public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    public Boolean userExistsById(@RequestParam("existsById") String userId) {
        return userRepository.existsById(userId);
    }
}
