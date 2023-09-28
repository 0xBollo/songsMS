package hwtb.ai.statisticsservice.repository;

import hwtb.ai.statisticsservice.model.PersonalSongStatistics;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PersonalSongStatisticsRepository extends MongoRepository<PersonalSongStatistics, ObjectId> {

    @Query("{'userId': ?0, 'songId': ?1}")
    Optional<PersonalSongStatistics> findByUserIdAndSongId(String userId, Integer songId);

    //@Query(value = "{}", sort = "{ retrievals: -1 }")
    List<PersonalSongStatistics> findTop3ByOrderByRetrievalsDesc();

    void deleteAllBySongId(Integer songId);
}
