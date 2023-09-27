package hwtb.ai.statisticsservice.dto.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor @Data @Builder
public class PersonalSongStatisticsResponse {

    private Integer songId;
    private Integer retrievals;
}
