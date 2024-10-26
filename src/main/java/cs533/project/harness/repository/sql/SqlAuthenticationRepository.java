package cs533.project.harness.repository.sql;

import cs533.project.harness.models.sql.SqlAuthenticationPojo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SqlAuthenticationRepository extends JpaRepository<SqlAuthenticationPojo, Long>
{
    Optional<SqlAuthenticationPojo> findByUserId(String userId);
}
