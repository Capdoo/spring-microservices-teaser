package rafael.projects.store.springserviceproduct.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="tbl_categories")
@Getter @Setter
@Data
@AllArgsConstructor @NoArgsConstructor @Builder
public class Category {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

}
