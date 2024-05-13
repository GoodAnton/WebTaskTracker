package leontev.webtasktracker.mapper;

import leontev.webtasktracker.dto.TaskStateDto;
import leontev.webtasktracker.entities.TaskState;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {TaskMapper.class})
public interface TaskStateMapper {

    TaskStateMapper INSTATNCE = Mappers.getMapper(TaskStateMapper.class);

    @Mapping(source = "taskList", target = "taskDtoList")
    TaskStateDto toDto (TaskState taskState);

    @Mapping(source = "taskDtoList", target = "taskList")
    TaskState toEntity (TaskStateDto taskStateDto);
}
