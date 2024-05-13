package leontev.webtasktracker.repositories;

import leontev.webtasktracker.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository  extends JpaRepository<Task, Long> {


}
