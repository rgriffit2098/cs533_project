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

    public String saveUserCredentials(String email)
    {
        String uuid = null;
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
            uuid = newMongoCredentialsPojo.getUserId();
        }
        //email exists
        else
        {
            log.error("Email {} already exists in credentials document in the mongo database", email);
        }

        return uuid;
    }

    public String retrieveUserId(String email)
    {
        String uuid = null;
        Optional<MongoCredentialsPojo> credentialsPojoOptional = mongoCredentialRepository.findByEmail(email);

        if(credentialsPojoOptional.isPresent())
        {
            MongoCredentialsPojo mongoCredentialsPojo = credentialsPojoOptional.get();
            uuid = mongoCredentialsPojo.getUserId();
        }

        return uuid;
    }

    public void deleteUserCredentials(String email)
    {
        Optional<MongoCredentialsPojo> credentialsPojoOptional = mongoCredentialRepository.findByEmail(email);

        if(credentialsPojoOptional.isPresent())
        {
            log.debug("Deleting email {} credentials from mongo database", email);
            mongoCredentialRepository.delete(credentialsPojoOptional.get());
        }
        //email doesn't exists
        else
        {
            log.error("Email {} does not exist in credentials document in the mongo database", email);
        }
    }
}
