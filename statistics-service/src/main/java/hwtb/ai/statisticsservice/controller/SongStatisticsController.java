package hwtb.ai.statisticsservice.controller;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;

import hwtb.ai.statisticsservice.dto.http.PersonalSongStatisticsResponse;
import hwtb.ai.statisticsservice.dto.http.SongStatisticsResponse;
import hwtb.ai.statisticsservice.exception.InvalidIdException;
import hwtb.ai.statisticsservice.exception.ResourceNotFoundException;
import hwtb.ai.statisticsservice.service.SongStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController @RequestMapping(path = "rest/songs")
public class SongStatisticsController {

    private final SongStatisticsService songStatisticsService;

    @GetMapping(path = "/{id}/stats")
    public ResponseEntity<SongStatisticsResponse> getStatisticsForSongWithId(@PathVariable("id") Integer songId)
            throws InvalidIdException, ResourceNotFoundException {
        SongStatisticsResponse songStatisticsResponse =
                songStatisticsService.getStatisticsForSongWithId(songId);

        return ResponseEntity.status(OK)
                .contentType(APPLICATION_JSON)
                .body(songStatisticsResponse);
    }

    @GetMapping(path = "/popular/stats")
    public ResponseEntity<List<SongStatisticsResponse>> getStatisticsForMostPopularSongs() {
        return ResponseEntity.status(OK)
                .contentType(APPLICATION_JSON)
                .body(songStatisticsService.getMostPopularSongStatistics());
    }

    @GetMapping(path = "/{id}/personal-stats")
    public ResponseEntity<PersonalSongStatisticsResponse> getPersonalStatisticsForSongWithId
            (@PathVariable("id") Integer songId, @RequestHeader("Subject") String userId)
            throws InvalidIdException, ResourceNotFoundException {
        PersonalSongStatisticsResponse personalSongStatisticsResponse =
                songStatisticsService.getPersonalStatisticsForSongWithId(userId, songId);

        return ResponseEntity.status(OK)
                .contentType(APPLICATION_JSON)
                .body(personalSongStatisticsResponse);
    }

    @GetMapping(path = "/popular/personal-stats")
    public ResponseEntity<List<PersonalSongStatisticsResponse>> getPersonalStatisticsForMostPopularSongs() {
        return ResponseEntity.status(OK)
                .contentType(APPLICATION_JSON)
                .body(songStatisticsService.getMostPopularPersonalSongStatistics());
    }
}
