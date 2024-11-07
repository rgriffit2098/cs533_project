package cs533.project.harness.data.mongo;

import cs533.project.harness.models.mongo.MongoAuthenticationPojo;
import cs533.project.harness.repository.mongo.MongoAuthenticationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
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

    public void saveUserAuthentication(String userId, String password)
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
    }

    public void updateUpdateUserAuthentication(String password)
    {
        List<MongoAuthenticationPojo> mongoAuthenticationPojos = mongoAuthenticationRepository.findAll();

        for(MongoAuthenticationPojo mongoAuthenticationPojo : mongoAuthenticationPojos)
        {
            mongoAuthenticationPojo.setPassword(password);
        }

        mongoAuthenticationRepository.saveAll(mongoAuthenticationPojos);
    }

    public void deleteUserAuthentication(String userId)
    {
        Optional<MongoAuthenticationPojo> authenticationPojoOptional = mongoAuthenticationRepository.findByUserId(userId);

        //delete authentication pojo
        if(authenticationPojoOptional.isPresent())
        {
            log.debug("Deleting authentication pojo with userId {} in mongo db", userId);
            mongoAuthenticationRepository.delete(authenticationPojoOptional.get());
        }
        else
        {
            log.error("No authentication entry found for userId {} in mongo db", userId);
        }
    }
}
