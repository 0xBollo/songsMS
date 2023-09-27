package hwtb.ai.statisticsservice.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter @Builder
@Document(collection = "song_statistics")
public class SongStatistics {

    @Id
    private ObjectId statisticsId;

    @Indexed(unique = true)
    private Integer songId;

    private Integer retrievals;
    List<SongUpdate> updateHistory;
}
