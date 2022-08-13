package bg.softuni.jwt.web;

import bg.softuni.jwt.exception.ExceptionHandler;
import bg.softuni.jwt.exception.UsernameExistsException;
import bg.softuni.jwt.model.User;
import bg.softuni.jwt.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class UserResource extends ExceptionHandler {
    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) throws UsernameExistsException {
        User registeredUser = userService.register(user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail());

        return new ResponseEntity<>(registeredUser, HttpStatus.OK);
    }
}