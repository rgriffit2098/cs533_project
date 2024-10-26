package cs533.project.harness.data.sql;

import cs533.project.harness.models.sql.SqlCredentialsPojo;
import cs533.project.harness.repository.sql.SqlCredentialRepository;
import lombok.extern.slf4j.Slf4j;
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

    public boolean saveUserCredentials(String email)
    {
        boolean success = false;
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
            success = true;
        }
        //email exists
        else
        {
            log.error("Email {} already exists in credentials document in the sql database", email);
        }

        return success;
    }
}
