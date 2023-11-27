package htwb.ai.songservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import htwb.ai.songservice.dto.SongMessage;
import htwb.ai.songservice.dto.SongUpdatedMessage;
import htwb.ai.songservice.model.Song;
import htwb.ai.songservice.repository.PlaylistRepository;
import htwb.ai.songservice.repository.SongRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
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
	public CommandLineRunner insertSongsIntoDB(SongRepository songRepository, RabbitTemplate rabbitTemplate,
											   PlaylistRepository playlistRepository) {
		return args -> {
			songRepository.deleteAll();
			playlistRepository.deleteAll();

			try (InputStream inputStream = getClass().getResourceAsStream("/songs.json")) {
				ObjectMapper objectMapper = new ObjectMapper();
				Song[] songs = objectMapper.readValue(inputStream, Song[].class);

				Arrays.stream(songs).forEach(song -> {
					songRepository.saveWithId(song);
					produceSongUpdatedMessage(song, rabbitTemplate);
				});
			}

			songRepository.syncSequence();
		};
	}

	@Value("${rabbitmq.exchange.name}")
	private String exchangeName;
	@Value("${rabbitmq.routing-key.song-updates}")
	private String songUpdatesRoutingKey;

	private void produceSongUpdatedMessage(Song song, RabbitTemplate rabbitTemplate) {
		SongUpdatedMessage songUpdatedMessage = SongUpdatedMessage.builder()
				.userId("jane")
				.songMessage(
						SongMessage.builder()
								.id(song.getId())
								.title(song.getTitle())
								.artist(song.getArtist())
								.label(song.getLabel())
								.released(song.getReleased())
								.build()
				)
				.build();
		rabbitTemplate.convertAndSend(exchangeName, songUpdatesRoutingKey, songUpdatedMessage);
	}
}
