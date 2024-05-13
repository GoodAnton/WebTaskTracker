package leontev.webtasktracker.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  //referencedColumnName

    @Column
    private String name;

    @Column(name = "created_date")
    @Builder.Default
    private Date createdDate = new Date();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @JoinColumn(name="project_id", referencedColumnName = "id") //в TaskState создает поле project_id, по которому собирается список
    private List<TaskState> taskStateList = new ArrayList<>();



}
