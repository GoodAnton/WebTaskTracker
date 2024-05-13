package leontev.webtasktracker.services.Impl;

import leontev.webtasktracker.dto.UserDto;
import leontev.webtasktracker.dto.UserFilterDto;
import leontev.webtasktracker.entities.User;
import leontev.webtasktracker.enums.Role;
import leontev.webtasktracker.exсeptions.NotFoundExeption;
import leontev.webtasktracker.mapper.UserMapper;
import leontev.webtasktracker.repositories.UserRepository;
import leontev.webtasktracker.services.UserService;
import leontev.webtasktracker.services.helpers.ServiceHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

   private final UserRepository userRepository;
    private final ServiceHelper serviceHelper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    public List<UserDto> getAllUsers(UserFilterDto userFilterDto) {
        return userRepository.findAllUsersByFilter(userFilterDto).stream()
                .map(UserMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    @Override
    public UserDto findUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            return UserMapper.INSTANCE.toDto(userOptional.get());
        } else {
            throw new NotFoundExeption(String.format("User with %s id doesn`t exist.", userId));
        }
    }

    public UserDto findUserByEmail(String email) {
        User user = userRepository.findByEmail(email).get();
        return UserMapper.INSTANCE.toDto(user);
    }

    @Override
    @Transactional
    public UserDto editUserByUser(Long userId, UserDto userDto) {
        User user = userRepository.findById(userId).get();
        user.setNickName(userDto.getNickName());
        user.setEmail(userDto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        return UserMapper.INSTANCE.toDto(user);
    }

    @Override
    @Transactional
    public UserDto editUserByAdmin(Long userId, UserDto userDto) {
        User user = userRepository.findById(userId).get();
            user.setRole(userDto.getRole());
        userRepository.save(user);
        return UserMapper.INSTANCE.toDto(user);
    }

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        serviceHelper.ifUserPresentThrowException(userDto.getEmail());
        System.out.println(userDto.getEmail());
        User user = userRepository.saveAndFlush(
                User.builder()
                        .nickName(userDto.getNickName())
                        .password(bCryptPasswordEncoder.encode(userDto.getPassword()))
                        .email(userDto.getEmail())
                        .role(Role.USER) // default
                        .build()
        );
        return UserMapper.INSTANCE.toDto(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);

    }

}
