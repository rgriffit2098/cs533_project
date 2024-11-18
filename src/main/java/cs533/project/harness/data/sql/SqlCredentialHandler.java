package cs533.project.harness.data.sql;

import cs533.project.harness.models.sql.SqlCredentialsPojo;
import cs533.project.harness.repository.sql.SqlCredentialRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class SqlCredentialHandler
{
    private final SqlCredentialRepository sqlCredentialRepository;

    public SqlCredentialHandler(final SqlCredentialRepository sqlCredentialRepository)
    {
        this.sqlCredentialRepository = sqlCredentialRepository;
    }

    @Retryable(retryFor = ObjectOptimisticLockingFailureException.class, maxAttempts = 10)
    public String saveUserCredentials(String email)
    {
        String uuid = null;
        Optional<SqlCredentialsPojo> credentialsPojoOptional = sqlCredentialRepository.findByEmail(email);

        //email doesn't exists
        if(credentialsPojoOptional.isEmpty())
        {
            log.debug("Saving new email {} credentials to sql database", email);
            SqlCredentialsPojo newSqlCredentialsPojo = SqlCredentialsPojo.builder()
                    .userId(UUID.randomUUID().toString())
                    .email(email)
                    .build();
            sqlCredentialRepository.save(newSqlCredentialsPojo);
            uuid = newSqlCredentialsPojo.getUserId();
        }
        //email exists
        else
        {
            log.error("Email {} already exists in credentials table in the sql database", email);
        }

        return uuid;
    }

    @Retryable(retryFor = ObjectOptimisticLockingFailureException.class, maxAttempts = 10)
    public String retrieveUserId(String email)
    {
        String uuid = null;
        Optional<SqlCredentialsPojo> credentialsPojoOptional = sqlCredentialRepository.findByEmail(email);

        if(credentialsPojoOptional.isPresent())
        {
            SqlCredentialsPojo sqlCredentialsPojo = credentialsPojoOptional.get();
            uuid = sqlCredentialsPojo.getUserId();
        }

        return uuid;
    }

    @Retryable(retryFor = ObjectOptimisticLockingFailureException.class, maxAttempts = 10)
    public void deleteUserCredentials(String email)
    {
        Optional<SqlCredentialsPojo> credentialsPojoOptional = sqlCredentialRepository.findByEmail(email);

        if(credentialsPojoOptional.isPresent())
        {
            log.debug("Deleting email {} credentials from sql database", email);
            sqlCredentialRepository.delete(credentialsPojoOptional.get());
        }
        //email doesn't exists
        else
        {
            log.error("Email {} does not exist in credentials table in the sql database", email);
        }
    }
}
