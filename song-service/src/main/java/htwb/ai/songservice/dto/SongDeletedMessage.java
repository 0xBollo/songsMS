package htwb.ai.songservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor @Data
public class SongDeletedMessage {

    Integer songId;
}
