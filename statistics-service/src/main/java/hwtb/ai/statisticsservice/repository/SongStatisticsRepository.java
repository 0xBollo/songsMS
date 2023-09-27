package hwtb.ai.statisticsservice.repository;

import hwtb.ai.statisticsservice.model.SongStatistics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SongStatisticsRepository extends MongoRepository<SongStatistics, String> {

    Optional<SongStatistics> findBySongId(Integer songId);

    @Query(value = "{}", sort = "{ retrievals: -1 }")
    List<SongStatistics> findTop3ByOrderByRetrievalsDesc();

    void deleteBySongId(Integer songId);
}
