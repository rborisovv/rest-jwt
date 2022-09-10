package bg.softuni.jwt.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UsersDto {
    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private String role;

    private Set<String> authorities;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Europe/Sofia")
    private Date joinDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Europe/Sofia")
    private Date lastLoginDate;

    private Boolean isNonLocked;

    private Boolean isActive;

    private String profileImageUrl;
}