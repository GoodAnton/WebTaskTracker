package leontev.webtasktracker.unit;

import leontev.webtasktracker.controllers.ProjectController;
import leontev.webtasktracker.dto.ProjectDto;
import leontev.webtasktracker.dto.UserDto;
import leontev.webtasktracker.services.Impl.ProjectServiceImpl;
import leontev.webtasktracker.services.Impl.TaskStateServiceImpl;
import leontev.webtasktracker.services.Impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = ProjectController.class)
@AutoConfigureMockMvc
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private  ProjectServiceImpl projectService;
    @MockBean
    private  UserServiceImpl userService;
    @MockBean
    private  TaskStateServiceImpl taskStateService;

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    public void testGetAllProjectsForUser()  throws  Exception {
    UserDto userDto = new UserDto().builder().userId(1l).email("test@email.com").build();

    when(userService.findUserByEmail(anyString())).thenReturn(userDto);
        when(projectService.getAllProjects(1l)).thenReturn(new ArrayList<>());

                mockMvc.perform(get("/projects")).andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void testGetAllProjectsForAnon()  throws  Exception {
        UserDto userDto = new UserDto().builder().userId(1l).email("test@email.com").build();

        when(userService.findUserByEmail(anyString())).thenReturn(userDto);
        when(projectService.getAllProjects(1l)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/projects")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    public void testGetProjectByIdForUser()  throws  Exception {
        UserDto userDto = new UserDto().builder().userId(1l).email("test@email.com").build();
        Long projectId =1l;
        Long userId = 1l;
        when(userService.findUserByEmail(anyString())).thenReturn(userDto);
        when(projectService.getProjectById(projectId,userId)).thenReturn(new ProjectDto());
        when(taskStateService.getTaskStates(projectId)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/projects/{project_id}",projectId)).andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void testGetProjectByIdForAnon()  throws  Exception {
        UserDto userDto = new UserDto().builder().userId(1l).email("test@email.com").build();
        Long projectId =1l;
        Long userId = 1l;
        when(userService.findUserByEmail(anyString())).thenReturn(userDto);
        when(projectService.getProjectById(projectId,userId)).thenReturn(new ProjectDto());
        when(taskStateService.getTaskStates(projectId)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/projects/{project_id}",projectId)).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    public void testCreateProjectForUser()  throws  Exception {

        when(projectService.createProject(anyString(), anyString())).thenReturn(new ProjectDto());

        mockMvc.perform(post("/projects")
                .with(csrf())).andExpect(status().is3xxRedirection());
    }

    @Test
    @WithAnonymousUser
    public void testCreateProjectForAnon()  throws  Exception {

        when(projectService.createProject(anyString(), anyString())).thenReturn(new ProjectDto());

        mockMvc.perform(post("/projects")
                .with(csrf())).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    public void testEditProjectForUser()  throws  Exception {

        when(projectService.editProject(anyLong(), anyString())).thenReturn(new ProjectDto()
                .builder().id(1l).build());

        mockMvc.perform(post("/projects/{project_id}/update",1l)
                .with(csrf())).andExpect(status().is3xxRedirection());

    }

    @Test
    @WithAnonymousUser
    public void testEditProjectForAnon()  throws  Exception {

        when(projectService.editProject(anyLong(), anyString())).thenReturn(new ProjectDto()
                .builder().id(1l).build());

        mockMvc.perform(post("/projects/{project_id}/update",1l)
                .with(csrf())).andExpect(status().isUnauthorized());

    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    public void testDeleteProjectForUser()  throws  Exception {

        mockMvc.perform(post("/projects/{project_id}/delete",1l)
                .with(csrf())).andExpect(status().is3xxRedirection());

    }

    @Test
    @WithAnonymousUser
    public void testDeleteProjectForAnon()  throws  Exception {

        mockMvc.perform(post("/projects/{project_id}/delete",1l)
                .with(csrf())).andExpect(status().isUnauthorized());

    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    public void testGetErrorPage()  throws  Exception {

        mockMvc.perform(get("/projects/error")).andExpect(status().isOk());

    }
    @Test
    @WithAnonymousUser
    public void testGetErrorPageForAnon()  throws  Exception {

        mockMvc.perform(get("/projects/error")).andExpect(status().isUnauthorized());

    }
}
