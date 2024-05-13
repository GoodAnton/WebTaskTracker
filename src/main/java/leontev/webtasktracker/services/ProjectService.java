package leontev.webtasktracker.services;

import leontev.webtasktracker.dto.ProjectDto;
import java.util.List;

public interface ProjectService {

    List<ProjectDto> getAllProjects();
    ProjectDto getProjectById(Long projectId, Long userId);
    ProjectDto createProject(String projectName, String email);
    ProjectDto editProject(Long projectId, String projectName);
    void deleteProject(Long projectId);


}
