package cs533.project.harness.data.sql;

import cs533.project.harness.models.sql.SqlPostPojo;
import cs533.project.harness.repository.sql.SqlPostsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@Transactional
public class SqlPostsHandler
{
    private final SqlPostsRepository sqlPostsRepository;

    public SqlPostsHandler(final SqlPostsRepository sqlPostsRepository)
    {
        this.sqlPostsRepository = sqlPostsRepository;
    }

    @Retryable(retryFor = ObjectOptimisticLockingFailureException.class, maxAttempts = 10)
    public void saveNewPost(String userId, String content)
    {
        log.debug("Saving new post created by userId {} in sql db", userId);
        SqlPostPojo sqlPostPojo = SqlPostPojo.builder()
                .postId(UUID.randomUUID().toString())
                .userId(userId)
                .content(content)
                .build();

        sqlPostsRepository.save(sqlPostPojo);
    }

    @Retryable(retryFor = ObjectOptimisticLockingFailureException.class, maxAttempts = 10)
    public void readPosts(String uuid)
    {
        sqlPostsRepository.findAllByUserId(uuid);
    }

    @Retryable(retryFor = ObjectOptimisticLockingFailureException.class, maxAttempts = 10)
    public void deletePosts(String uuid)
    {
        sqlPostsRepository.deleteAllByUserId(uuid);
    }
}
