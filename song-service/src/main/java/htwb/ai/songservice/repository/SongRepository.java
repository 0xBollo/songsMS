package htwb.ai.songservice.repository;

import htwb.ai.songservice.model.Song;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO songs (id, title, artist, label, released) VALUES (:#{#song.id}, :#{#song.title}, " +
            ":#{#song.artist}, :#{#song.label}, :#{#song.released})", nativeQuery = true)
    void saveWithId(@Param("song") Song song);

    @Query(value = "SELECT setval(pg_get_serial_sequence('songs', 'id'), COALESCE(MAX(id), 0) + 1, false) FROM songs",
            nativeQuery = true)
    void syncSequence();
}
