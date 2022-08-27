package bg.softuni.jwt.web;

import bg.softuni.jwt.domain.HTTPResponse;
import bg.softuni.jwt.dto.NewUserDto;
import bg.softuni.jwt.dto.UpdateUserDto;
import bg.softuni.jwt.exception.UserExistsException;
import bg.softuni.jwt.exception.UserNotFoundException;
import bg.softuni.jwt.model.User;
import bg.softuni.jwt.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static bg.softuni.jwt.common.Messages.USER_DELETED_SUCCESSFULLY;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/user")
public class UserResource {
    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<NewUserDto> addNewUser(@RequestParam("firstName") String firstName,
                                                 @RequestParam("lastName") String lastName,
                                                 @RequestParam("username") String username,
                                                 @RequestParam("email") String email,
                                                 @RequestParam("role") String role,
                                                 @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) throws UserExistsException, IOException {

        NewUserDto newUserDto = new NewUserDto(firstName, lastName, username, email, role, profileImage);
        return userService.addNewUser(newUserDto);
    }

    @PostMapping("/update")
    public ResponseEntity<UpdateUserDto> updateUser(@RequestParam("currentUsername") String currentUsername,
                                                    @RequestParam("firstName") String firstName,
                                                    @RequestParam("lastName") String lastName,
                                                    @RequestParam("email") String email,
                                                    @RequestParam("role") String role,
                                                    @RequestParam("isNonLocked") String isNonLocked,
                                                    @RequestParam("isActive") String isActive,
                                                    @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) throws UserExistsException, IOException {

        UpdateUserDto updatedUserDto = new UpdateUserDto(currentUsername, firstName, lastName,
                email, role, Boolean.parseBoolean(isNonLocked), Boolean.parseBoolean(isActive), profileImage);
        return userService.updateUser(updatedUserDto);
    }

    @PostMapping("/updateProfileImage")
    public ResponseEntity<User> updateProfileImage(@RequestParam("username") String username,
                                                            @RequestParam("profileImage") MultipartFile profileImage) throws UserExistsException, IOException, UserNotFoundException {

        User user = userService.updateProfileImage(username, profileImage);
        return new ResponseEntity<>(user, OK);
    }

    @GetMapping("/find/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) throws UserNotFoundException {
        return userService.findUserByUsername(username);
    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> getAllUsers() {
        return userService.findAllUsers();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('DELETE')")
    public ResponseEntity<HTTPResponse> deletedUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return response(NO_CONTENT, USER_DELETED_SUCCESSFULLY);
    }

    private ResponseEntity<HTTPResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HTTPResponse(httpStatus.value(), httpStatus,
                httpStatus.getReasonPhrase().toUpperCase(Locale.ROOT), message), httpStatus);
    }
}