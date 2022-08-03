package bg.softuni.jwt.model;

import bg.softuni.jwt.enumeration.Authority;
import bg.softuni.jwt.enumeration.Role;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table
public class User extends BaseEntity implements Serializable {

    @Column
    private String userId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true, updatable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, updatable = false)
    private String email;

    @Column(nullable = false)
    private String profileImgUrl;

    @Column(nullable = false)
    private LocalDate lastLoginDate;

    @Column(nullable = false)
    private LocalDate getLastLoginDateDisplay;

    @Column(nullable = false)
    private LocalDate joinDate;

    @ElementCollection
    @Column(nullable = false)
    private Set<Role> roles;

    @ElementCollection
    @Column(nullable = false)
    private Set<Authority> authorities;

    @Column(nullable = false)
    private Boolean isActive;

    @Column(nullable = false)
    private Boolean isNotLocked;
}