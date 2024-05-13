package leontev.webtasktracker.services;

import leontev.webtasktracker.dto.TaskStateDto;
import java.util.List;

public interface TaskStateService {

    List<TaskStateDto> getTaskStates(Long projectId);
    TaskStateDto createTaskState(Long projectId, TaskStateDto taskStateDto);
    TaskStateDto editTaskState(Long taskStateId, TaskStateDto taskStateDto);
    void deleteTaskState(Long taskStateId);

}
