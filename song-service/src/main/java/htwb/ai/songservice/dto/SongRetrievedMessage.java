package htwb.ai.songservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor @Data @Builder
public class SongRetrievedMessage {

    private Integer songId;
    private String userId;
}
