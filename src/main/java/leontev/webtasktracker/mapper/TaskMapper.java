package leontev.webtasktracker.mapper;

import leontev.webtasktracker.dto.TaskDto;
import leontev.webtasktracker.entities.Task;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    TaskDto toDto(Task task);

    Task toEntity(TaskDto taskDto);
}
