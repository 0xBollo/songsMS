package htwb.ai.authservice.dto;

import lombok.Getter;

@Getter
public class UserRequest {

    private String userId;
    private String password;
    private String firstName;
    private String lastName;
}
