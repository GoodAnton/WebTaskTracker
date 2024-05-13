package leontev.webtasktracker.services;

import leontev.webtasktracker.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();
    UserDto findUserById(Long userId);
    UserDto createUser(UserDto userDto);
    UserDto editUserByUser(Long userId, UserDto userDto);
    UserDto editUserByAdmin(Long userId, UserDto userDto);
    void deleteUser(Long userId);

}
