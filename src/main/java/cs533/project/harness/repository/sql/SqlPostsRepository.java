package cs533.project.harness.repository.sql;

import cs533.project.harness.models.sql.SqlPostPojo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SqlPostsRepository extends JpaRepository<SqlPostPojo, Long>
{
    Optional<SqlPostPojo> findByUserId(String postId);
}
