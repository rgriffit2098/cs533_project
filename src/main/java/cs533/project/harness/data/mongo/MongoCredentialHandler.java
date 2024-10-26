package cs533.project.harness.data.mongo;

import cs533.project.harness.models.mongo.MongoCredentialsPojo;
import cs533.project.harness.repository.mongo.MongoCredentialRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class MongoCredentialHandler
{
    private final MongoCredentialRepository mongoCredentialRepository;

    public MongoCredentialHandler(final MongoCredentialRepository mongoCredentialRepository)
    {
        this.mongoCredentialRepository = mongoCredentialRepository;
    }

    public boolean saveUserCredentials(String email)
    {
        boolean success = false;
        Optional<MongoCredentialsPojo> credentialsPojoOptional = mongoCredentialRepository.findByEmail(email);

        //email doesn't exists
        if(credentialsPojoOptional.isEmpty())
        {
            log.debug("Saving new email {} credentials to mongo database", email);
            MongoCredentialsPojo newMongoCredentialsPojo = MongoCredentialsPojo.builder()
                    .userId(UUID.randomUUID().toString())
                    .email(email)
                    .build();
            mongoCredentialRepository.save(newMongoCredentialsPojo);
            success = true;
        }
        //email exists
        else
        {
            log.error("Email {} already exists in credentials document in the mongo database", email);
        }

        return success;
    }
}
