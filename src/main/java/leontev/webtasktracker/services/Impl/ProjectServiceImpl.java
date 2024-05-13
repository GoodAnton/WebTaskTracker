package leontev.webtasktracker.services.Impl;

import leontev.webtasktracker.dto.ProjectDto;
import leontev.webtasktracker.entities.Project;
import leontev.webtasktracker.entities.User;
import leontev.webtasktracker.mapper.ProjectMapper;
import leontev.webtasktracker.repositories.ProjectRepository;
import leontev.webtasktracker.repositories.UserRepository;
import leontev.webtasktracker.services.ProjectService;
import leontev.webtasktracker.services.helpers.ServiceHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional (readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ServiceHelper serviceHelper;

    @Override
    public List<ProjectDto> getAllProjects() {

        return projectRepository.findAll().stream()
                .map(ProjectMapper.INSTANCE::toDto)
                .collect(Collectors.toList());

    }

    public List<ProjectDto> getAllProjects(Long userId) {

        return projectRepository.findProjectsByUserId(userId).stream()
                .map(ProjectMapper.INSTANCE::toDto)
                .collect(Collectors.toList());

    }

    @Override
    public ProjectDto getProjectById(Long projectId, Long userId) {
        return ProjectMapper.INSTANCE.toDto(
                projectRepository.findProjectByUserId(projectId, userId).get());
                  }

    @Override
    @Transactional
    public ProjectDto createProject(String projectName, String email) {

        Project project = projectRepository.saveAndFlush(Project.builder()
                .name(projectName)
                .build());
        User user = userRepository.findByEmail(email).get();
        user.getProjectList().add(project);
        userRepository.save(user);
        return ProjectMapper.INSTANCE.toDto(project);
    }

    @Override
    @Transactional
    public ProjectDto editProject(Long projectId, String projectName) {
        Project project = serviceHelper.getProjectOrThrowException(projectId);

        project.setName(projectName);
        project = projectRepository.saveAndFlush(project);
        return ProjectMapper.INSTANCE.toDto(project);
    }

    @Override
    @Transactional
    public void deleteProject(Long projectId) {
        serviceHelper.getProjectOrThrowException(projectId);
        projectRepository.deleteById(projectId);
    }






}
