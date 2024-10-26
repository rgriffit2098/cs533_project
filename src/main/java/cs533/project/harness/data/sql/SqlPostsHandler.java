package cs533.project.harness.data.sql;

import cs533.project.harness.models.sql.SqlPostPojo;
import cs533.project.harness.repository.sql.SqlPostsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class SqlPostsHandler
{
    private final SqlPostsRepository sqlPostsRepository;

    public SqlPostsHandler(final SqlPostsRepository sqlPostsRepository)
    {
        this.sqlPostsRepository = sqlPostsRepository;
    }

    public boolean saveNewPost(String userId, String content)
    {
        log.debug("Saving new post created by userId {} in sql db", userId);
        SqlPostPojo sqlPostPojo = SqlPostPojo.builder()
                .postId(UUID.randomUUID().toString())
                .userId(userId)
                .content(content)
                .build();

        sqlPostsRepository.save(sqlPostPojo);
        return true;
    }
}
