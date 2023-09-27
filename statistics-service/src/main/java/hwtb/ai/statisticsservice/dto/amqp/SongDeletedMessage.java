package hwtb.ai.statisticsservice.dto.amqp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor @Data
public class SongDeletedMessage {

    Integer songId;
}
