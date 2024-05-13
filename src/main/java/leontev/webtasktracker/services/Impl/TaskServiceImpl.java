package leontev.webtasktracker.services.Impl;

import leontev.webtasktracker.dto.TaskDto;
import leontev.webtasktracker.entities.Task;
import leontev.webtasktracker.entities.TaskState;
import leontev.webtasktracker.mapper.TaskMapper;
import leontev.webtasktracker.repositories.TaskRepository;
import leontev.webtasktracker.repositories.TaskStateRepository;
import leontev.webtasktracker.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskStateRepository taskStateRepository;

    @Override
    public List<TaskDto> getTasks(Long taskStateId) {
        TaskState taskState = taskStateRepository.findById(taskStateId).get();

        return taskState.getTaskList().stream()
                .map(TaskMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TaskDto createTask(Long taskStateId, TaskDto taskDto) {

        Task task = taskRepository.saveAndFlush(
                Task.builder()
                        .name(taskDto.getName())
                        .description(taskDto.getDescription())
                        .build()
        );
        TaskState taskState = taskStateRepository.findById(taskStateId).get();
        taskState.getTaskList().add(task);
        taskStateRepository.save(taskState);

        return TaskMapper.INSTANCE.toDto(task);
    }

    @Override
    @Transactional
    public TaskDto editTask(Long taskId, TaskDto taskDto) {

        Task task = taskRepository.findById(taskId).get();
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        taskRepository.saveAndFlush(task);

        return TaskMapper.INSTANCE.toDto(task);
    }

    @Override
    @Transactional
    public void deleteTask(Long taskId) {
    taskRepository.deleteById(taskId);
    }
}
