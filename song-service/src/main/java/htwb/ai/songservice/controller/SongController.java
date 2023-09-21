package htwb.ai.songservice.controller;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;

import htwb.ai.songservice.exception.*;
import htwb.ai.songservice.model.Song;
import htwb.ai.songservice.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController @RequestMapping(path = "rest/songs")
public class SongController {

    private final SongRepository songRepository;

    @GetMapping
    public ResponseEntity<List<Song>> getAllSongs() {
        List<Song> songs = songRepository.findAll();
        return ResponseEntity.status(OK).contentType(APPLICATION_JSON)
                .body(songs);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Song> getSongWithId(@PathVariable("id") Integer id)
            throws InvalidIdException, ResourceNotFoundException {

        if (id < 1)
            throw new InvalidIdException("ID cannot be less than 1");

        Optional<Song> songOptional = songRepository.findById(id);

        if (songOptional.isEmpty())
            throw new ResourceNotFoundException("Song", "ID", id);

        return ResponseEntity.status(OK).contentType(APPLICATION_JSON)
                .body(songOptional.get());
    }

    @PostMapping
    public ResponseEntity<Integer> createSong(@RequestBody Song song)
            throws ForbiddenIdAssignmentException, InvalidAttributeValueException {

        if (song.getId() != null)
            throw new ForbiddenIdAssignmentException("Song must not have an ID yet");

        if (! isValidSong(song))
            throw new InvalidAttributeValueException("Attributes must not be empty " +
                    "and released year must be between 1600 and now");

        int songId = songRepository.save(song).getId();
        return ResponseEntity.status(CREATED)
                .location(UriComponentsBuilder.fromPath("/rest/songs/{id}").build(songId))
                .build();
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Void> updateSong(@PathVariable("id") Integer id, @RequestBody Song songUpdate)
            throws InvalidIdException, ResourceNotFoundException, InvalidAttributeValueException,
                IdMismatchException {

        if (id < 1)
            throw new InvalidIdException("ID cannot be less than 1");

        if (! songRepository.existsById(id))
            throw new ResourceNotFoundException("Song", "ID", id);

        if (! isValidSong(songUpdate))
            throw new InvalidAttributeValueException("Attributes must not be empty " +
                    "and released year must be between 1600 and now");

        if ((songUpdate.getId() != null) && (! songUpdate.getId().equals(id)))
            throw new IdMismatchException("ID in body must match ID in URL or may not exist at all");

        Song song = songRepository.findById(id).get();
        song.setTitle(songUpdate.getTitle());
        song.setArtist(songUpdate.getArtist());
        song.setLabel(songUpdate.getLabel());
        song.setReleased(songUpdate.getReleased());
        songRepository.save(song);

        return ResponseEntity.status(NO_CONTENT).build();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable("id") Integer id)
            throws InvalidIdException, ResourceNotFoundException {

        if (id < 1)
            throw new InvalidIdException("ID cannot be less than 1");

        if (! songRepository.existsById(id))
            throw new ResourceNotFoundException("Song", "ID", id);

        songRepository.deleteById(id);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    private boolean isValidSong(Song song) {
        return ! (song.getTitle() == null || song.getTitle().isBlank() ||
                song.getArtist() == null || song.getArtist().isBlank() ||
                song.getLabel() == null || song.getLabel().isBlank() ||
                song.getReleased() < 1600 || song.getReleased() > LocalDate.now().getYear());
    }
}