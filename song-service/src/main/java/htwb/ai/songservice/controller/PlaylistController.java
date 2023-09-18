package htwb.ai.songservice.controller;

import htwb.ai.songservice.dto.PlaylistRequest;
import htwb.ai.songservice.dto.PlaylistResponse;
import htwb.ai.songservice.exception.*;
import htwb.ai.songservice.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;


@RequiredArgsConstructor
@RestController @RequestMapping(path = "rest/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<PlaylistResponse> getPlaylistWithId(@PathVariable("id") Integer id,
                                                              @RequestHeader("Subject") String userId)
            throws InvalidIdException, ResourceNotFoundException, ForbiddenResourceAccessException {
        if (id < 1)
            throw new InvalidIdException("ID cannot be less than 1");

        //String userId = authorizeRequest(headers);   // throws AuthorizationException
        PlaylistResponse playlistResponse = playlistService.getPlaylistWithId(id);   // throws ResourceNotFoundException

        if (playlistResponse.getIsPrivate() && (! playlistResponse.getOwnerId().equals(userId)))
            throw new ForbiddenResourceAccessException("Playlist", "ID", id);

        return ResponseEntity.status(OK).contentType(APPLICATION_JSON).body(playlistResponse);
    }

    @GetMapping
    public ResponseEntity<List<PlaylistResponse>> getPlaylistsFromUser(@RequestParam("userId") String fromUserId,
                                                                       @RequestHeader("Subject") String forUserId)
            throws ResourceNotFoundException {

        //String forUserId = authorizeRequest(headers);   // throws AuthorizationException

        List<PlaylistResponse> playlistResponses =
                playlistService.getPlaylistsFromUserForUser(fromUserId, forUserId);

        return ResponseEntity.status(OK).contentType(APPLICATION_JSON).body(playlistResponses);
    }

    @PostMapping
    public ResponseEntity<Void> postPlaylist(@RequestBody PlaylistRequest playlistRequest,
                                             @RequestHeader("Subject") String userId)
            throws InvalidAttributeValueException {
        //String userId = authorizeRequest(headers);   // throws AuthorizationException

        int id = playlistService.createPlaylist(playlistRequest, userId);   // throws InvalidAttributeValueException

        return ResponseEntity.created(UriComponentsBuilder.fromPath("/rest/playlists/{id}").build(id)).build();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable("id") Integer id, @RequestHeader("Subject") String userId)
            throws InvalidIdException, ForbiddenResourceAccessException {
        if (id < 1)
            throw new InvalidIdException("ID cannot be less than 1");

        //String userId = authorizeRequest(headers);   // throws AuthorizationException

        playlistService.deletePlaylist(id, userId);   // throws ResourceNotFoundException, ForbiddenRessourceAccessException

        return ResponseEntity.noContent().build();
    }
}
