package bg.softuni.jwt.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table
public class User extends BaseEntity implements Serializable {

    @Column
    @NonNull
    private String userId;

    @NonNull
    @Column(nullable = false)
    private String firstName;

    @NonNull
    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true, updatable = false)
    @NonNull
    private String username;

    @NonNull
    @Column(columnDefinition = "TEXT", nullable = false)
    private String password;

    @NonNull
    @Column(nullable = false, unique = true, updatable = false)
    private String email;

    @NonNull
    @Column(nullable = false)
    private String profileImgUrl;

    @Column
    private Date lastLoginDate;

    @Column
    private Date LastLoginDateDisplay;

    @NonNull
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy hh:mm:ss", timezone = "Europe/Sofia")
    private Date joinDate;

    @NonNull
    @Column(nullable = false)
    private String role;

    @NonNull
    @ElementCollection
    @Column(nullable = false)
    private Set<String> authorities;

    @NonNull
    @Column(nullable = false)
    private Boolean isActive;

    @NonNull
    @Column(nullable = false)
    private Boolean isNotLocked;
}