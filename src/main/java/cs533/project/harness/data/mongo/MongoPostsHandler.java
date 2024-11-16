package cs533.project.harness.data.mongo;

import cs533.project.harness.models.mongo.MongoPostPojo;
import cs533.project.harness.repository.mongo.MongoPostsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class MongoPostsHandler
{
    private final MongoPostsRepository mongoPostsRepository;

    public MongoPostsHandler(final MongoPostsRepository mongoPostsRepository)
    {
        this.mongoPostsRepository = mongoPostsRepository;
    }

    public void saveNewPost(String userId, String content)
    {
        log.debug("Saving new post created by userId {} in mongo db", userId);
        MongoPostPojo mongoPostPojo = MongoPostPojo.builder()
                .postId(UUID.randomUUID().toString())
                .userId(userId)
                .content(content)
                .build();

        mongoPostsRepository.save(mongoPostPojo);
    }

    public void readPosts(String uuid)
    {
        mongoPostsRepository.findAllByUserId(uuid);
    }

    public void deletePosts(String uuid)
    {
        mongoPostsRepository.deleteAllByUserId(uuid);
    }
}
