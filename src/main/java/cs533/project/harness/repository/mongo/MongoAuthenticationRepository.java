package cs533.project.harness.repository.mongo;

import cs533.project.harness.models.mongo.MongoAuthenticationPojo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MongoAuthenticationRepository extends MongoRepository<MongoAuthenticationPojo, String>
{
    Optional<MongoAuthenticationPojo> findByUserId(String userId);
}
