package bg.softuni.jwt.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserLoginDto {
    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 6)
    private String password;
}