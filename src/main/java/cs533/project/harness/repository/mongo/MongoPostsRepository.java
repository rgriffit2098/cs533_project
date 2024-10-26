package cs533.project.harness.repository.mongo;

import cs533.project.harness.models.mongo.MongoPostPojo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MongoPostsRepository extends MongoRepository<MongoPostPojo, String>
{
    Optional<MongoPostPojo> findByUserId(String postId);
}
