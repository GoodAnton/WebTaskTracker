package leontev.webtasktracker.entities;

import jakarta.persistence.*;
import leontev.webtasktracker.enums.Phase;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "task_state")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //referencedColumnName


    private String name;

    @Enumerated(EnumType.STRING)
    private Phase phase;

    @ManyToOne
    private Project project;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @JoinColumn(name="task_state_id", referencedColumnName = "id") //в Task создает поле task_state_id, по которому собирается список
    private List<Task> taskList = new ArrayList<>();
}
