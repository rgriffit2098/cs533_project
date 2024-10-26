package cs533.project.harness.models.sql;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class SqlPostPojo
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String postId;
    private String userId;
    private String content;
}
