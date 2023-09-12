package htwb.ai.authservice.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
@Entity @Table(name = "users")
public class User {

    @Id
    private String id;
    private String password;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
}