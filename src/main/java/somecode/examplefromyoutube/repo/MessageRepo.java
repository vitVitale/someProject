package somecode.examplefromyoutube.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import somecode.examplefromyoutube.domain.Message;

public interface MessageRepo extends JpaRepository<Message, Long> {
}
