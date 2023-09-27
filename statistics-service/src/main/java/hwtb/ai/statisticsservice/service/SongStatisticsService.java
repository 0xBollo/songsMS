package hwtb.ai.statisticsservice.service;

import hwtb.ai.statisticsservice.dto.amqp.*;
import hwtb.ai.statisticsservice.dto.http.PersonalSongStatisticsResponse;
import hwtb.ai.statisticsservice.dto.http.SongResponse;
import hwtb.ai.statisticsservice.dto.http.SongStatisticsResponse;
import hwtb.ai.statisticsservice.dto.http.SongUpdateResponse;
import hwtb.ai.statisticsservice.exception.InvalidIdException;
import hwtb.ai.statisticsservice.exception.ResourceNotFoundException;
import hwtb.ai.statisticsservice.model.PersonalSongStatistics;
import hwtb.ai.statisticsservice.model.Song;
import hwtb.ai.statisticsservice.model.SongStatistics;
import hwtb.ai.statisticsservice.model.SongUpdate;
import hwtb.ai.statisticsservice.repository.PersonalSongStatisticsRepository;
import hwtb.ai.statisticsservice.repository.SongStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class SongStatisticsService {

    private final SongStatisticsRepository songStatisticsRepository;
    private final PersonalSongStatisticsRepository personalSongStatisticsRepository;

    public SongStatisticsResponse getStatisticsForSongWithId(int id)
            throws InvalidIdException, ResourceNotFoundException {
        if (id < 1)
            throw new InvalidIdException("ID cannot be less than 1");

        Optional<SongStatistics> songStatisticsOptional = songStatisticsRepository.findBySongId(id);

        if (songStatisticsOptional.isEmpty())
            throw new ResourceNotFoundException("Song", "ID", id);

        SongStatistics songStatistics = songStatisticsOptional.get();
        return mapToSongStatisticsResponse(songStatistics);
    }

    public List<SongStatisticsResponse> getMostPopularSongStatistics() {
        return songStatisticsRepository.findTop3ByOrderByRetrievalsDesc().stream()
                .map(this::mapToSongStatisticsResponse)
                .toList();
    }

    public PersonalSongStatisticsResponse getPersonalStatisticsForSongWithId(String userId, Integer songId)
            throws InvalidIdException, ResourceNotFoundException {
        if (songId < 1)
            throw new InvalidIdException("ID cannot be less than 1");

        Optional<PersonalSongStatistics> personalSongStatisticsOptional =
                personalSongStatisticsRepository.findByUserIdAndSongId(userId, songId);

        if (personalSongStatisticsOptional.isEmpty())
            throw new ResourceNotFoundException();

        return mapToPersonalSongStatisticsResponse(personalSongStatisticsOptional.get());
    }

    public List<PersonalSongStatisticsResponse> getMostPopularPersonalSongStatistics() {
        return personalSongStatisticsRepository.findTop3ByOrderByRetrievalsDesc().stream()
                .map(this::mapToPersonalSongStatisticsResponse)
                .toList();
    }

    @RabbitListener(queues = {"${rabbitmq.queue.song-retrievals.name}"})
    public void onSongRetrieval(SongRetrievedMessage songRetrievalMessage) {
        // Update SongStatistics
        SongStatistics songStatistics =
                songStatisticsRepository.findBySongId(songRetrievalMessage.getSongId()).get();

        songStatistics.setRetrievals(songStatistics.getRetrievals() + 1);
        songStatisticsRepository.save(songStatistics);

        // PersonalSongStatistics
        Optional<PersonalSongStatistics> personalSongStatisticsOptional =
                personalSongStatisticsRepository.findByUserIdAndSongId(songRetrievalMessage.getUserId(),
                        songRetrievalMessage.getSongId());

        // Create PersonalSongStatistics on first retrieval
        if (personalSongStatisticsOptional.isEmpty()) {
            PersonalSongStatistics personalSongStatistics = PersonalSongStatistics.builder()
                    .userId(songRetrievalMessage.getUserId())
                    .songId(songRetrievalMessage.getSongId())
                    .retrievals(1)
                    .build();

            personalSongStatisticsRepository.save(personalSongStatistics);
            return;
        }

        // Update PersonalSongStatistics
        PersonalSongStatistics personalSongStatistics = personalSongStatisticsOptional.get();
        personalSongStatistics.setRetrievals(personalSongStatistics.getRetrievals() + 1);
        personalSongStatisticsRepository.save(personalSongStatistics);
    }

    @RabbitListener(queues = {"${rabbitmq.queue.song-updates.name}"})
    public void onSongUpdate(SongUpdatedMessage songUpdateMessage) {
        Integer songId = songUpdateMessage.getSongMessage().getId();
        Optional<SongStatistics> songStatisticsOptional = songStatisticsRepository.findBySongId(songId);

        SongUpdate songUpdate = mapToSongUpdate(songUpdateMessage);

        // Create SongStatistics if song is newly created
        if (songStatisticsOptional.isEmpty()) {
            SongStatistics songStatistics = SongStatistics.builder()
                    .songId(songId)
                    .retrievals(0)
                    .updateHistory(List.of(songUpdate))
                    .build();

            songStatisticsRepository.save(songStatistics);
            return;
        }

        // Update SongStatistics
        SongStatistics songStatistics = songStatisticsOptional.get();
        songStatistics.getUpdateHistory().add(0, songUpdate);
        songStatisticsRepository.save(songStatistics);
    }

    @RabbitListener(queues = {"${rabbitmq.queue.song-deletions.name}"})
    public void onSongDeletion(SongDeletedMessage songDeletedMessage) {
        songStatisticsRepository.deleteBySongId(songDeletedMessage.getSongId());
        personalSongStatisticsRepository.deleteAllBySongId(songDeletedMessage.getSongId());
    }

    private SongResponse mapToSongResponse(Song song) {
        return SongResponse.builder()
                .title(song.getTitle())
                .artist(song.getArtist())
                .label(song.getLabel())
                .released(song.getReleased())
                .build();
    }

    private SongUpdateResponse mapToSongUpdateResponse(SongUpdate songUpdate) {
        return SongUpdateResponse.builder()
                .userId(songUpdate.getUserId())
                .song(mapToSongResponse(songUpdate.getSong()))
                .build();
    }

    private SongStatisticsResponse mapToSongStatisticsResponse(SongStatistics songStatistics) {
        return SongStatisticsResponse.builder()
                .songId(songStatistics.getSongId())
                .retrievals(songStatistics.getRetrievals())
                .updateHistory(songStatistics.getUpdateHistory().stream()
                        .map(this::mapToSongUpdateResponse)
                        .toList())
                .build();
    }

    private PersonalSongStatisticsResponse mapToPersonalSongStatisticsResponse
            (PersonalSongStatistics personalSongStatistics) {
        return PersonalSongStatisticsResponse.builder()
                .songId(personalSongStatistics.getSongId())
                .retrievals(personalSongStatistics.getRetrievals())
                .build();
    }

    private Song mapToSong(SongMessage songMessage) {
        return Song.builder()
                .title(songMessage.getTitle())
                .artist(songMessage.getArtist())
                .label(songMessage.getLabel())
                .released(songMessage.getReleased())
                .build();
    }

    private SongUpdate mapToSongUpdate(SongUpdatedMessage songUpdatedMessage) {
        return SongUpdate.builder()
                .userId(songUpdatedMessage.getUserId())
                .song(mapToSong(songUpdatedMessage.getSongMessage()))
                .build();
    }
}
