package bg.softuni.jwt.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
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
    @ManyToOne
    private Role role;

    @NonNull
    @Column(nullable = false)
    private Boolean isActive;

    @NonNull
    @Column(nullable = false)
    private Boolean isNonLocked;
}