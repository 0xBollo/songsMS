package hwtb.ai.statisticsservice.dto.amqp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class SongRetrievedMessage {

    private Integer songId;
    private String userId;
}
