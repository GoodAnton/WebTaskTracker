package leontev.webtasktracker.dto;

import jakarta.validation.constraints.NotBlank;
import leontev.webtasktracker.enums.Phase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskStateDto {

    private Long id;

    @NotBlank
    private String name;

    private Phase phase;

    private List<TaskDto> taskDtoList = new ArrayList<>();
}
