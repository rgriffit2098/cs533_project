package cs533.project.harness.repository.mongo;

import cs533.project.harness.models.mongo.MongoCredentialsPojo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MongoCredentialRepository extends MongoRepository<MongoCredentialsPojo, String>
{
    Optional<MongoCredentialsPojo> findByEmail(String email);
}
