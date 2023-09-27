package hwtb.ai.statisticsservice.dto.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@AllArgsConstructor @Data @Builder
public class SongStatisticsResponse {

    private Integer songId;
    private Integer retrievals;
    List<SongUpdateResponse> updateHistory;
}
