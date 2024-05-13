package leontev.webtasktracker.mapper;

import leontev.webtasktracker.dto.ProjectDto;
import leontev.webtasktracker.entities.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {TaskStateMapper.class})
public interface ProjectMapper {

    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    @Mapping(source = "taskStateList", target = "taskStateDtoList")
    ProjectDto toDto(Project project);

    @Mapping(source = "taskStateDtoList", target = "taskStateList")
    Project toEntity(ProjectDto projectDto);
}
