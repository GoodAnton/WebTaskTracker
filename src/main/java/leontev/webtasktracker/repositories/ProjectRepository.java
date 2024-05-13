package leontev.webtasktracker.repositories;

import leontev.webtasktracker.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
//Аннотацию Repository добавлять не нужно, т.к. экстендимся от JPA
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findByName(String name);

    @Query(nativeQuery = true, value = "SELECT * FROM project WHERE user_id=?")
    List<Project> findProjectsByUserId( Long userId);

    @Query(nativeQuery = true, value = "SELECT * FROM project WHERE project.id=? AND user_id=?")
    Optional<Project> findProjectByUserId(Long projectId, Long userId);



}
