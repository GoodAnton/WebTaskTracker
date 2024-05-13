package leontev.webtasktracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import leontev.webtasktracker.enums.Role;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long userId;

    @NotBlank
    private String nickName;

    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;

    private Role role;

    private List<ProjectDto> projectDtoList = new ArrayList<>();

}
