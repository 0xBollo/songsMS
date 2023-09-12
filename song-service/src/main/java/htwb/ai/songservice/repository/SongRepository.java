package htwb.ai.songservice.repository;

import htwb.ai.songservice.model.Song;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends CrudRepository<Song, Integer> {

    List<Song> findAll();

    @Query(
            "SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END " +
                    "FROM Song s " +
                    "WHERE s = :song"
    )
    boolean existsBySong(Song song);
}
