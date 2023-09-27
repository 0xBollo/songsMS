package hwtb.ai.statisticsservice.dto.amqp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class SongMessage {

    private Integer id;
    private String title;
    private String artist;
    private String label;
    private int released;
}
