package htwb.ai.songservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import htwb.ai.songservice.model.Song;
import htwb.ai.songservice.repository.SongRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.InputStream;
import java.util.Arrays;


@SpringBootApplication
public class SongServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SongServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner insertSongsIntoDB(SongRepository songRepository) {
		return args -> {
			songRepository.deleteAll();

			try (InputStream inputStream = getClass().getResourceAsStream("/songs.json")) {
				ObjectMapper objectMapper = new ObjectMapper();
				Song[] songs = objectMapper.readValue(inputStream, Song[].class);

				Arrays.stream(songs).forEach(songRepository::saveWithId);
			}
		};
	}
}
