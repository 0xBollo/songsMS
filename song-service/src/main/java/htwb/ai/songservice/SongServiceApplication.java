package htwb.ai.songservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import htwb.ai.songservice.model.Song;
import htwb.ai.songservice.repository.SongRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.util.Arrays;


@SpringBootApplication
public class SongServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SongServiceApplication.class, args);
	}

	// FIXME File songs.json not found
	/*@Bean
	public CommandLineRunner insertUsersIntoDB(SongRepository songRepository) {
		return args -> {
			songRepository.deleteAll();

			ObjectMapper objectMapper = new ObjectMapper();
			Song[] songs = objectMapper.readValue(new File("songs.json"), Song[].class);

			songRepository.saveAll(Arrays.asList(songs));
		};
	}*/
}
