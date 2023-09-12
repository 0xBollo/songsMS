package htwb.ai.songservice.dto;

import htwb.ai.songservice.model.Song;
import lombok.Data;

import java.util.List;

@Data
public class PlaylistRequest {

    private String name;
    private Boolean isPrivate;
    private List<Song> songs;
}