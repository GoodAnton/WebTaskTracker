package leontev.webtasktracker.entities;

import jakarta.persistence.*;
import leontev.webtasktracker.enums.Role;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "user_table") //  "user" не создает, видимо из за конфилктов в нэйминге...
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId; // Пришлось переимоновать, потому что MapStruct для этой сущности не хотел мапить id!!!

    private String nickName;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", referencedColumnName = "userId") //в Project создает поле user_id, по которому собирается список
    private List<Project> projectList = new ArrayList<>();
}
