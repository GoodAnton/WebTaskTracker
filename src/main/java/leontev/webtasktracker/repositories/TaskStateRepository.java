package leontev.webtasktracker.repositories;

import leontev.webtasktracker.entities.TaskState;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TaskStateRepository extends JpaRepository<TaskState, Long> {

}
