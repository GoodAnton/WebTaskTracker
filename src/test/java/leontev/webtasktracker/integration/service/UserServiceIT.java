package leontev.webtasktracker.integration.service;

import leontev.webtasktracker.dto.UserDto;
import leontev.webtasktracker.dto.UserFilterDto;
import leontev.webtasktracker.enums.Role;
import leontev.webtasktracker.repositories.UserRepository;
import leontev.webtasktracker.services.Impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceIT {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testGetAllUsers() {

        List<UserDto> actualUsers = userService.getAllUsers();
        int expectedUserCount = 8;
        assertEquals(actualUsers.size(), expectedUserCount);

    }
    @Test
    public void testGetAllUsersWithFilter() {
        UserFilterDto userFilterDto1 = new UserFilterDto();
        UserFilterDto userFilterDto2 = new UserFilterDto("TEST", "TEST@mail.ru");
        List<UserDto> actualUsers1 = userService.getAllUsers(userFilterDto1);
        List<UserDto> actualUsers2 = userService.getAllUsers(userFilterDto2);
        int expectedUserCount1 = 8;
        int expectedUserCount2 = 1;
        assertEquals(actualUsers1.size(), expectedUserCount1);
        assertEquals(actualUsers2.size(), expectedUserCount2);
    }

    @Test
    public void testFindUserById() {
        UserDto actualResult = userService.findUserById(35l);
        UserDto expectedResult = UserDto.builder()
                .userId(35l)
                .nickName("TEST")
//                .password()
                .role(Role.USER)
                .email("TEST@mail.ru")
                .projectDtoList(new ArrayList<>())
                .build();
        assertEquals(expectedResult.getNickName(), actualResult.getNickName());
        assertEquals(expectedResult.getUserId(), actualResult.getUserId());
        assertEquals(expectedResult.getEmail(), actualResult.getEmail());
        assertEquals(expectedResult.getRole(), actualResult.getRole());
        assertEquals(expectedResult.getProjectDtoList(), actualResult.getProjectDtoList());
    }

    @Test
    public void testFindUserByEmail() {
        UserDto actualResult = userService.findUserByEmail("TEST@mail.ru");
        UserDto expectedResult = UserDto.builder()
                .userId(35l)
                .nickName("TEST")
//                .password()
                .role(Role.USER)
                .email("TEST@mail.ru")
                .projectDtoList(new ArrayList<>())
                .build();
        assertEquals(expectedResult.getNickName(), actualResult.getNickName());
        assertEquals(expectedResult.getUserId(), actualResult.getUserId());
        assertEquals(expectedResult.getEmail(), actualResult.getEmail());
        assertEquals(expectedResult.getRole(), actualResult.getRole());
        assertEquals(expectedResult.getProjectDtoList(), actualResult.getProjectDtoList());
    }

    @Test
    @Transactional
    public void testCreateUser() {

        UserDto userDto = UserDto.builder()
                .nickName("TEST1")
                .password("password")
                .email("TEST1@mail.ru")
                .role(Role.USER)
                .build();

        UserDto actualResult = userService.createUser(userDto);

        assertNotNull(actualResult);
        assertNotNull(actualResult.getUserId());
        assertNotNull(actualResult.getPassword());
        assertEquals(userDto.getNickName(), actualResult.getNickName());
        assertEquals(userDto.getEmail(), actualResult.getEmail());
        assertEquals(userDto.getRole(), actualResult.getRole());


    }

    @Test
    @Transactional
    public void testEditUserByUser() {
        Long userId = 35l;
        UserDto userDto = UserDto.builder()
                .nickName("TEST1")
                .password("password")
                .email("TEST1@mail.ru")
                .build();

        UserDto actualResult = userService.editUserByUser(userId, userDto);

        assertNotNull(actualResult);
        assertNotNull(actualResult.getPassword());
        assertNotNull(actualResult.getUserId());
        assertEquals(userDto.getNickName(), actualResult.getNickName());
        assertEquals(userDto.getEmail(), actualResult.getEmail());

    }

    @Test
    @Transactional
    public void testEditUserByAdmin() {
        Long userId = 35l;
        UserDto userDto = UserDto.builder()
                .role(Role.ADMIN)
                .build();

        UserDto actualResult = userService.editUserByAdmin(userId, userDto);

        assertNotNull(actualResult);
        assertNotNull(actualResult.getUserId());
        assertEquals(userDto.getRole(), actualResult.getRole());

    }

    @Test
    @Transactional
    public void testDeleteUser() {
        UserDto userDto = UserDto.builder()
                .nickName("TEST1")
                .password("password")
                .email("TEST1@mail.ru")
                .build();

        UserDto createdUser = userService.createUser(userDto);

        userService.deleteUser(createdUser.getUserId());

        boolean userExists = userRepository.existsById(createdUser.getUserId());
        assertFalse(userExists);

    }
}
