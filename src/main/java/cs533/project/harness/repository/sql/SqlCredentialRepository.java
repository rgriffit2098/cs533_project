package cs533.project.harness.repository.sql;

import cs533.project.harness.models.sql.SqlCredentialsPojo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SqlCredentialRepository extends JpaRepository<SqlCredentialsPojo, Long>
{
    Optional<SqlCredentialsPojo> findByEmail(String email);
}
