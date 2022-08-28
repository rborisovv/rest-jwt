package bg.softuni.jwt.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table
public class Role extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @ToString.Exclude
    @Column(nullable = false)
    @ElementCollection(fetch = FetchType.LAZY)
    private Set<String> authorities;
}