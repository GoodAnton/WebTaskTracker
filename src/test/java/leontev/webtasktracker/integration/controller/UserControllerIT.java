package leontev.webtasktracker.integration.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import leontev.webtasktracker.dto.UserDto;
import leontev.webtasktracker.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void testGetAllUsersForAdmin() throws Exception {
        ResultActions result = mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    public void testGetAllUsersForUser() throws Exception {
        ResultActions result = mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON));
        result.andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    public void testGetAllUsersForAnon() throws Exception {
        ResultActions result = mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON));
        result.andExpect(status().is3xxRedirection());
        result.andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    public void testGetUserByIdForUser() throws Exception {
        ResultActions result = mockMvc.perform(get("/users/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void testGetUserByIdForAnon() throws Exception {
        ResultActions result = mockMvc.perform(get("/users/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON));
        result.andExpect(status().is3xxRedirection());
    }

    @Test
    @WithAnonymousUser
    @Transactional
    public void testCreateUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setNickName("testUser");
        userDto.setEmail("testUser@example.com");
        userDto.setPassword("password");

        ResultActions result = mockMvc.perform(post("/users")
                        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)));

        result.andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    @Transactional
    public void testEditUserByUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setNickName("testUser");
        userDto.setEmail("testUser@example.com");
        userDto.setPassword("password");

        ResultActions result = mockMvc.perform(post("/users/{user_id}/updateByUser", 2L)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)));

        result.andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    @Transactional
    public void testEditRoleUserByAdmin() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setRole(Role.ADMIN);

        ResultActions result = mockMvc.perform(post("/users/{user_id}/updateByAdmin", 2L)
                        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)));

        result.andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Transactional
    public void testDeleteUserByAdmin() throws Exception {
        ResultActions result = mockMvc.perform(post("/users/{user_id}/delete", 2L)
                        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().is3xxRedirection()); // Проверяем редирект после успешного удаления пользователя
    }


}
