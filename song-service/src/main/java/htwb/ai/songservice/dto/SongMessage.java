package htwb.ai.songservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor @Data @Builder
public class SongMessage {

    private Integer id;
    private String title;
    private String artist;
    private String label;
    private int released;
}
