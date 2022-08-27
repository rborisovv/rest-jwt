package bg.softuni.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class UpdateUserDto {

    //String currentUsername, String newFirstName, String newLastName,
    //                           String newEmail, String role, boolean isNonLocked, boolean isActive,
    //                           MultipartFile profileImage

    @NotBlank
    private String currentUsername;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String role;

    private Boolean isNonLocked;

    private Boolean isActive;

    private MultipartFile multipartFile;
}
