package htwb.ai.songservice.dto;

import htwb.ai.songservice.model.Song;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data @Builder
public class PlaylistResponse {

    private int id;
    private String name;
    private String ownerId;
    private Boolean isPrivate;
    private List<Song> songs;
}