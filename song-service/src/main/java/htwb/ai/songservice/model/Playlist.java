package htwb.ai.songservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
@Entity @Table(name = "playlists")
public class Playlist {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "owner_id")
    private String ownerId;
    private String name;
    @Column(name = "is_private")
    private Boolean isPrivate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "playlists_songs",
            joinColumns = @JoinColumn(name = "playlist"),
            inverseJoinColumns = @JoinColumn(name = "song")
    )
    private List<Song> songs;
}