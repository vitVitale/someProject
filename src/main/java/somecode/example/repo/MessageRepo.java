package somecode.example.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import somecode.example.domain.Message;

public interface MessageRepo extends JpaRepository<Message, Long> {
}
