package cs533.project.harness.models.mongo;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document("Credentials")
public class MongoCredentialsPojo
{
    @Id
    private String id;
    private String userId;
    private String email;
}
