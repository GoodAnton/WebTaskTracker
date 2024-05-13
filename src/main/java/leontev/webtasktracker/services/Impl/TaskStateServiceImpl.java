package leontev.webtasktracker.services.Impl;

import leontev.webtasktracker.dto.TaskStateDto;
import leontev.webtasktracker.entities.Project;
import leontev.webtasktracker.entities.TaskState;
import leontev.webtasktracker.mapper.TaskStateMapper;
import leontev.webtasktracker.repositories.ProjectRepository;
import leontev.webtasktracker.repositories.TaskStateRepository;
import leontev.webtasktracker.services.TaskStateService;
import leontev.webtasktracker.services.helpers.ServiceHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional (readOnly = true)
public class TaskStateServiceImpl implements TaskStateService {

    private final TaskStateRepository taskStateRepository;
    private final ProjectRepository projectRepository;
    private final ServiceHelper serviceHelper;

    @Override
    public List<TaskStateDto> getTaskStates(Long projectId) {
        Project project = serviceHelper.getProjectOrThrowException(projectId);

        return project.getTaskStateList().stream()
                .map(TaskStateMapper.INSTATNCE::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TaskStateDto createTaskState(Long projectId, TaskStateDto taskStateDto) {

        TaskState taskState = taskStateRepository.saveAndFlush(
                TaskState.builder()
                        .name(taskStateDto.getName())
                        .phase(taskStateDto.getPhase())
                        .build()
        );
        Project project = projectRepository.findById(projectId).get();
        project.getTaskStateList().add(taskState);
        projectRepository.save(project);
        return TaskStateMapper.INSTATNCE.toDto(taskState);
    }

    @Override
    @Transactional
    public TaskStateDto editTaskState(Long taskStateId, TaskStateDto taskStateDto) {
        TaskState taskState = taskStateRepository.findById(taskStateId).get();
        taskState.setName(taskStateDto.getName());
        taskState.setPhase(taskStateDto.getPhase());

        taskStateRepository.saveAndFlush(taskState);
        return TaskStateMapper.INSTATNCE.toDto(taskState);

    }

    @Override
    @Transactional
    public void deleteTaskState(Long taskStateId) {
        taskStateRepository.deleteById(taskStateId);
    }
}

