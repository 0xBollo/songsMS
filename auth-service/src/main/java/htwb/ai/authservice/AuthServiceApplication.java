package htwb.ai.authservice;

import htwb.ai.authservice.model.User;
import htwb.ai.authservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner insertUsersIntoDB(UserRepository userRepository) {
		return args -> {
			userRepository.deleteAll();

			User user1 = User.builder()
					.id("maxime")
					.password("pass1234")
					.firstName("Maxime")
					.lastName("Muster")
					.build();
			User user2 = User.builder()
					.id("jane")
					.password("pass1234")
					.firstName("Jane")
					.lastName("Doe")
					.build();

			userRepository.save(user1);
			userRepository.save(user2);
		};
	}
}
