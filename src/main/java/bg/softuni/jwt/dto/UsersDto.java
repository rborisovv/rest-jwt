package bg.softuni.jwt.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UsersDto {
    private String firstName;

    private String lastName;

    private String email;

    private String role;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy hh:mm:ss", timezone = "Europe/Sofia")
    private Date joinDate;

    private Boolean isNonLocked;

    private Boolean isActive;

    private String profileImageUrl;
}