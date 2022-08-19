package bg.softuni.jwt.web;

import bg.softuni.jwt.dto.UserLoginDto;
import bg.softuni.jwt.dto.UserRegisterDto;
import bg.softuni.jwt.exception.ExceptionHandler;
import bg.softuni.jwt.exception.UsernameExistsException;
import bg.softuni.jwt.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/user")
public class UserResource extends ExceptionHandler {
    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterDto> register(@RequestBody @Valid UserRegisterDto userRegisterDto) throws UsernameExistsException {
        UserRegisterDto registeredUser = userService.register(userRegisterDto);
        return new ResponseEntity<>(registeredUser, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginDto> login(@RequestBody @Valid UserLoginDto userLoginDto) {
        return userService.login(userLoginDto);
    }
}