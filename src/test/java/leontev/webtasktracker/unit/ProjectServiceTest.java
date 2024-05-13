package leontev.webtasktracker.unit;

import leontev.webtasktracker.dto.ProjectDto;
import leontev.webtasktracker.entities.Project;
import leontev.webtasktracker.entities.User;
import leontev.webtasktracker.repositories.ProjectRepository;
import leontev.webtasktracker.repositories.UserRepository;
import leontev.webtasktracker.services.Impl.ProjectServiceImpl;
import leontev.webtasktracker.services.helpers.ServiceHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {
    @Mock
    private  ProjectRepository projectRepository;
    @Mock
    private  UserRepository userRepository;
    @Mock
    private  ServiceHelper serviceHelper;
    @InjectMocks
    private ProjectServiceImpl projectService;

    @Test
    public void testGetAllProjects() {
        Long userId = 1L;
        Project project1 = new Project();
        Project project2 = new Project();
        List<Project> projects = Arrays.asList(project1, project2);
        when(projectRepository.findProjectsByUserId(userId)).thenReturn(projects);

        List<ProjectDto> projectDtos = projectService.getAllProjects(userId);

        assertEquals(2, projectDtos.size());
        assertNotNull(projectDtos);
    }

    @Test
    public void testGetProjectById() {
        Long userId = 1L;
        Long projectId = 1L;
        Project projectFromRepo = Project.builder()
                .id(projectId)
                .name("testProject")
                .createdDate(new Date())
                .taskStateList(new ArrayList<>()).build();

        when(projectRepository.findProjectByUserId(projectId,userId)).thenReturn(Optional.of(projectFromRepo));

        ProjectDto projectDtoFromService = projectService.getProjectById(userId, projectId);

        assertNotNull(projectDtoFromService);
        assertEquals(projectFromRepo.getId(), projectDtoFromService.getId());
        assertEquals(projectFromRepo.getName(), projectDtoFromService.getName());
        assertEquals(projectFromRepo.getCreatedDate(), projectDtoFromService.getCreatedDate());
        assertEquals(projectFromRepo.getTaskStateList(), projectDtoFromService.getTaskStateDtoList());

    }

    @Test
    public void testCreateProject() {
        String projectName = "testProject";
        String email = "test@email.com";

        User userFromRepo = User.builder().userId(1l).email(email).projectList(new ArrayList<>()).build();
        Project projectFromRepo = Project.builder().id(1l).name(projectName).build();


        when(projectRepository.saveAndFlush(any(Project.class))).thenReturn(projectFromRepo);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userFromRepo));
                when(userRepository.save(any(User.class))).thenReturn(userFromRepo);

        ProjectDto projectDtoFromService = projectService.createProject(projectName, email);

        assertNotNull(projectDtoFromService);
        assertEquals(projectName, projectDtoFromService.getName());
        assertEquals(1, userFromRepo.getProjectList().size());
    }

    @Test
    public void testEditProject() {
        Long projectId = 1l;
        String projectName = "testName";
        Project projectFromRepo = Project.builder().id(1l).build();


        when(serviceHelper.getProjectOrThrowException(projectId)).thenReturn(projectFromRepo);
        when(projectRepository.saveAndFlush(any(Project.class))).thenReturn(projectFromRepo);

        ProjectDto projectDtoFromService = projectService.editProject(projectId, projectName);

        assertNotNull(projectDtoFromService);
        assertEquals(projectName, projectDtoFromService.getName());
    }

    @Test
    public void testDeleteProject() {
        Long projectId = 1l;
        Project projectFromRepo = Project.builder().id(1l).build();

        when(serviceHelper.getProjectOrThrowException(projectId)).thenReturn(projectFromRepo);
        doNothing().when(projectRepository).deleteById(projectId);

        projectService.deleteProject(projectId);

        verify(serviceHelper, times(1)).getProjectOrThrowException(projectId);
        verify(projectRepository, times(1)).deleteById(projectId);

    }

}
