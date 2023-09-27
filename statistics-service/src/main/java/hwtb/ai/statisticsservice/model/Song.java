package hwtb.ai.statisticsservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class Song {

    String title;
    String artist;
    String label;
    Integer released;
}
