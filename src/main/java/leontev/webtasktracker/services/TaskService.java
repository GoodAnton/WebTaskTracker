package leontev.webtasktracker.services;

import leontev.webtasktracker.dto.TaskDto;
import java.util.List;

public interface TaskService {

    List<TaskDto> getTasks(Long taskStateId);
    TaskDto createTask(Long taskStateId, TaskDto taskDto);
    TaskDto editTask(Long taskId, TaskDto taskDto);
    void deleteTask(Long taskId);

}
