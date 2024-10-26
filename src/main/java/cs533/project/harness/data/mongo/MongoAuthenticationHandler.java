package cs533.project.harness.data.mongo;

import cs533.project.harness.models.mongo.MongoAuthenticationPojo;
import cs533.project.harness.repository.mongo.MongoAuthenticationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class MongoAuthenticationHandler
{
    private final MongoAuthenticationRepository mongoAuthenticationRepository;

    public MongoAuthenticationHandler(final MongoAuthenticationRepository mongoAuthenticationRepository)
    {
        this.mongoAuthenticationRepository = mongoAuthenticationRepository;
    }

    public boolean saveUserAuthentication(String userId, String password)
    {
        Optional<MongoAuthenticationPojo> authenticationPojoOptional = mongoAuthenticationRepository.findByUserId(userId);

        //create authentication pojo and save password
        if(authenticationPojoOptional.isEmpty())
        {
            log.debug("Saving new password for userId {} in mongo db", userId);
            MongoAuthenticationPojo mongoAuthenticationPojo = MongoAuthenticationPojo.builder()
                    .userId(userId)
                    .password(password)
                    .build();
            mongoAuthenticationRepository.save(mongoAuthenticationPojo);
        }
        //update password
        else
        {
            log.debug("Updating password for userId {} in mongo db", userId);
            MongoAuthenticationPojo mongoAuthenticationPojo = authenticationPojoOptional.get();
            mongoAuthenticationPojo.setPassword(password);
            mongoAuthenticationRepository.save(mongoAuthenticationPojo);
        }

        return true;
    }
}
