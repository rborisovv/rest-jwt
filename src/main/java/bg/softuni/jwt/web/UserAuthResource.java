package bg.softuni.jwt.web;

import bg.softuni.jwt.dto.UserLoginDto;
import bg.softuni.jwt.dto.UserRegisterDto;
import bg.softuni.jwt.exception.UserExistsException;
import bg.softuni.jwt.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/user")
@CrossOrigin("http://localhost:4200")
public class UserAuthResource {
    private final UserService userService;

    public UserAuthResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterDto> register(@RequestBody @Valid UserRegisterDto userRegisterDto) throws UserExistsException {
        return userService.register(userRegisterDto);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginDto> login(@RequestBody @Valid UserLoginDto userLoginDto) {
        return userService.login(userLoginDto);
    }
}