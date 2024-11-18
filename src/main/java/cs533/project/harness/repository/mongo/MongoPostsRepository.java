package cs533.project.harness.repository.mongo;

import cs533.project.harness.models.mongo.MongoPostPojo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongoPostsRepository extends MongoRepository<MongoPostPojo, String>
{
    List<MongoPostPojo> findAllByUserId(String userId);
    void deleteAllByUserId(String userId);
}
