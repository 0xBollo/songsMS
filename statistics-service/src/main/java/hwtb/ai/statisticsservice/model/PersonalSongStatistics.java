package hwtb.ai.statisticsservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
@Document(collection = "personal-song-statistics")
@CompoundIndexes({
        @CompoundIndex(name = "userId_songId_index", def = "{'userId' : 1, 'songId' : 1}", unique = true)
})
public class PersonalSongStatistics {

    @Id
    private ObjectId statisticsId;
    private String userId;
    private Integer songId;
    private Integer retrievals;
}
