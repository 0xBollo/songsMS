package htwb.ai.songservice.repository;

import htwb.ai.songservice.model.Playlist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends CrudRepository<Playlist, Integer> {

    List<Playlist> findByOwnerId(String ownerId);
    List<Playlist> findByOwnerIdAndIsPrivate(String ownerId, boolean isPrivate);
    @Query("SELECT p.ownerId FROM Playlist p WHERE p.id = :id")
    String findOwnerIdById(Integer id);
}