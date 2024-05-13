package leontev.webtasktracker.repositories;

import leontev.webtasktracker.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, FilterRepository {

    Optional<User> findByEmail(String email);
}
