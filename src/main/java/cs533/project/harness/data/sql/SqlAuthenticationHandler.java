package cs533.project.harness.data.sql;

import cs533.project.harness.models.sql.SqlAuthenticationPojo;
import cs533.project.harness.repository.sql.SqlAuthenticationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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

    public boolean saveUserAuthentication(String userId, String password)
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
        //update password
        else
        {
            log.debug("Updating password for userId {} in sql db", userId);
            SqlAuthenticationPojo sqlAuthenticationPojo = authenticationPojoOptional.get();
            sqlAuthenticationPojo.setPassword(password);
            sqlAuthenticationRepository.save(sqlAuthenticationPojo);
        }

        return true;
    }
}
