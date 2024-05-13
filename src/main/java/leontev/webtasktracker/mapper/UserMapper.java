package leontev.webtasktracker.mapper;

import leontev.webtasktracker.dto.UserDto;
import leontev.webtasktracker.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {ProjectMapper.class})
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "projectList", target = "projectDtoList")
    UserDto toDto(User user);

    @Mapping(source = "projectDtoList", target = "projectList")
    User toEntity(UserDto userDto);
}
