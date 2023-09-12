package htwb.ai.songservice.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Data @Builder @EqualsAndHashCode
@Entity @Table(name = "songs")
public class Song {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String artist;
    private String label;
    private int released;
}