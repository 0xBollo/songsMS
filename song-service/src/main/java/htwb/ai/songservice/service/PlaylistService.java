package htwb.ai.songservice.service;

import htwb.ai.songservice.dto.*;
import htwb.ai.songservice.exception.ForbiddenResourceAccessException;
import htwb.ai.songservice.exception.InvalidAttributeValueException;
import htwb.ai.songservice.exception.ResourceNotFoundException;
import htwb.ai.songservice.model.Playlist;
import htwb.ai.songservice.repository.PlaylistRepository;
import htwb.ai.songservice.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final SongRepository songRepository;
    private final WebClient.Builder webClientBuilder;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;
    @Value("${rabbitmq.routing-key.song-retrievals}")
    private String songRetrievalsRoutingKey;

    public PlaylistResponse getPlaylistWithId(int id, String subject)
            throws ResourceNotFoundException, ForbiddenResourceAccessException {
        Optional<Playlist> playlistOptional = playlistRepository.findById(id);
        if (playlistOptional.isEmpty())
            throw new ResourceNotFoundException("Playlist", "ID", id);

        Playlist playlist = playlistOptional.get();

        if (playlist.getIsPrivate() && (! playlist.getOwnerId().equals(subject)))
            throw new ForbiddenResourceAccessException("Playlist", "ID", id);

        produceSongRetrievedMessages(playlist, subject);

        return PlaylistResponse.builder()
                .id(playlist.getId())
                .ownerId(playlist.getOwnerId())
                .name(playlist.getName())
                .isPrivate(playlist.getIsPrivate())
                .songs(playlist.getSongs())
                .build();
    }

    public List<PlaylistResponse> getPlaylistsFromUserForUser(String fromUserId, String forUserId)
            throws ResourceNotFoundException {
        // Check if the user exists
        Boolean userExists = webClientBuilder.build().get()
                .uri(String.format("http://auth-service/rest/users?existsById=%s", fromUserId))
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (Boolean.FALSE.equals(userExists))
            throw new ResourceNotFoundException("User", "ID", fromUserId);

        List<Playlist> playlists =  fromUserId.equals(forUserId) ?
                playlistRepository.findByOwnerId(fromUserId) :
                playlistRepository.findByOwnerIdAndIsPrivate(fromUserId, false);

        playlists.forEach(playlist -> produceSongRetrievedMessages(playlist, forUserId));

        return playlists.stream()
                .map(playlist ->
                        PlaylistResponse.builder()
                                .id(playlist.getId())
                                .ownerId(playlist.getOwnerId())
                                .name(playlist.getName())
                                .isPrivate(playlist.getIsPrivate())
                                .songs(playlist.getSongs())
                                .build())
                .toList();
    }

    public int createPlaylist(PlaylistRequest playlistRequest, String ownerId) throws InvalidAttributeValueException {
        if (! isValidPlaylistRequest(playlistRequest))
            throw new InvalidAttributeValueException("Missing or empty attributes");

        Playlist playlist = Playlist.builder()
                .ownerId(ownerId)
                .name(playlistRequest.getName())
                .isPrivate(playlistRequest.getIsPrivate())
                .songs(playlistRequest.getSongs())
                .build();

        playlist.getSongs().forEach(song -> {
            if ((! songRepository.existsBySong(song)) || Collections.frequency(playlist.getSongs(), song) > 1)
                throw new InvalidAttributeValueException("Contains non existing song");
        });

        return playlistRepository.save(playlist).getId();
    }

    public void updatePlaylist(int id, PlaylistRequest playlistUpdate, String userId)
            throws InvalidAttributeValueException, ResourceNotFoundException, ForbiddenResourceAccessException {
        if (! isValidPlaylistRequest(playlistUpdate))
            throw new InvalidAttributeValueException("Missing or empty attributes");

        Optional<Playlist> playlistOptional = playlistRepository.findById(id);
        if (playlistOptional.isEmpty())
            throw new ResourceNotFoundException("Playlist", "ID", id);
        Playlist playlist = playlistOptional.get();

        if (! playlist.getOwnerId().equals(userId))
            throw new ForbiddenResourceAccessException("Playlist", "ID", id);

        playlist.setName(playlistUpdate.getName());
        playlist.setIsPrivate(playlistUpdate.getIsPrivate());
        playlist.setSongs(playlistUpdate.getSongs());

        playlist.getSongs().forEach(song -> {
            if ((! songRepository.existsBySong(song)) || Collections.frequency(playlist.getSongs(), song) > 1)
                throw new InvalidAttributeValueException("Contains non existing song");
        });

        playlistRepository.save(playlist);
    }

    public void deletePlaylist(int playlistId, String ownerId)
            throws ResourceNotFoundException, ForbiddenResourceAccessException {
        if (! playlistRepository.existsById(playlistId))
            throw new ResourceNotFoundException("Playlist", "ID", playlistId);

        if (! ownerId.equals(playlistRepository.findOwnerIdByPlaylistId(playlistId)))
            throw new ForbiddenResourceAccessException("Playlist", "ID", playlistId);

        playlistRepository.deleteById(playlistId);
    }

    private boolean isValidPlaylistRequest(PlaylistRequest playlistRequest) {
        return ! (playlistRequest.getName() == null || playlistRequest.getName().isBlank() ||
                playlistRequest.getIsPrivate() == null || playlistRequest.getSongs() == null);
    }

    private void produceSongRetrievedMessages(Playlist playlist, String subject) {
        playlist.getSongs().forEach(song -> {
            SongRetrievedMessage songRetrievedMessage = SongRetrievedMessage.builder()
                    .songId(song.getId())
                    .userId(subject)
                    .build();
            System.out.printf("%n%n%n%n%nRETRIEVED SONG WITH ID = %d%n%n%n%n%n", song.getId());
            rabbitTemplate.convertAndSend(exchangeName, songRetrievalsRoutingKey, songRetrievedMessage);
        });
    }
}
