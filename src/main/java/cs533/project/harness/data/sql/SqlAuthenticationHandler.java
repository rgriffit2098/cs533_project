package cs533.project.harness.data.sql;

import cs533.project.harness.models.sql.SqlAuthenticationPojo;
import cs533.project.harness.repository.sql.SqlAuthenticationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class SqlAuthenticationHandler
{
    private final SqlAuthenticationRepository sqlAuthenticationRepository;

    public SqlAuthenticationHandler(final SqlAuthenticationRepository sqlAuthenticationRepository)
    {
        this.sqlAuthenticationRepository = sqlAuthenticationRepository;
    }

    @Retryable(retryFor = ObjectOptimisticLockingFailureException.class, maxAttempts = 10)
    public void saveUserAuthentication(String userId, String password)
    {
        Optional<SqlAuthenticationPojo> authenticationPojoOptional = sqlAuthenticationRepository.findByUserId(userId);

        //create authentication pojo and save password
        if(authenticationPojoOptional.isEmpty())
        {
            log.debug("Saving new password for userId {} in sql db", userId);
            SqlAuthenticationPojo sqlAuthenticationPojo = SqlAuthenticationPojo.builder()
                    .userId(userId)
                    .password(password)
                    .build();
            sqlAuthenticationRepository.save(sqlAuthenticationPojo);
        }
    }

    @Retryable(retryFor = ObjectOptimisticLockingFailureException.class, maxAttempts = 10)
    public void updateUpdateUserAuthentication(String password)
    {
        List<SqlAuthenticationPojo> sqlAuthenticationPojos = sqlAuthenticationRepository.findAll();

        for(SqlAuthenticationPojo sqlAuthenticationPojo : sqlAuthenticationPojos)
        {
            sqlAuthenticationPojo.setPassword(password);
        }

        sqlAuthenticationRepository.saveAll(sqlAuthenticationPojos);
    }

    @Retryable(retryFor = ObjectOptimisticLockingFailureException.class, maxAttempts = 10)
    public void deleteUserAuthentication(String userId)
    {
        Optional<SqlAuthenticationPojo> authenticationPojoOptional = sqlAuthenticationRepository.findByUserId(userId);

        //delete authentication pojo
        if(authenticationPojoOptional.isPresent())
        {
            log.debug("Deleting authentication pojo with userId {} in sql db", userId);
            sqlAuthenticationRepository.delete(authenticationPojoOptional.get());
        }
        else
        {
            log.error("No authentication entry found for userId {} in sql db", userId);
        }
    }
}
