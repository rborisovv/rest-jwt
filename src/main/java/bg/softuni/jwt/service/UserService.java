package bg.softuni.jwt.service;

import bg.softuni.jwt.common.ExceptionMessages;
import bg.softuni.jwt.dao.RoleRepository;
import bg.softuni.jwt.dao.UserRepository;
import bg.softuni.jwt.dto.NewUserDto;
import bg.softuni.jwt.dto.UpdateUserDto;
import bg.softuni.jwt.dto.UserLoginDto;
import bg.softuni.jwt.dto.UserRegisterDto;
import bg.softuni.jwt.enumeration.Role;
import bg.softuni.jwt.exception.UserExistsException;
import bg.softuni.jwt.exception.UserNotFoundException;
import bg.softuni.jwt.model.User;
import bg.softuni.jwt.util.JWTProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static bg.softuni.jwt.common.ExceptionMessages.USER_BY_USERNAME_NOT_FOUND;
import static bg.softuni.jwt.common.ExceptionMessages.USER_NOT_FOUND;
import static bg.softuni.jwt.common.FileConstant.*;
import static bg.softuni.jwt.common.SecurityConstant.JWT_TOKEN_HEADER;
import static bg.softuni.jwt.enumeration.Role.USER;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Service
@Transactional
@Qualifier("UserService")
public class UserService {
    private static final String USER_LOGGER_REGISTER_INFO = "User with %s username successfully registered!";

    @Value("${user.default.password}")
    private String defaultPassword;
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JWTProvider jwtProvider;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService, AuthenticationManager authenticationManager, JWTProvider jwtProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    public ResponseEntity<NewUserDto> addNewUser(NewUserDto newUserDto) throws UserExistsException, IOException {
        String username = newUserDto.getUsername();
        String email = newUserDto.getEmail();
        validateRegisterCredentials(username, email);

        String firstName = newUserDto.getFirstName();
        String lastName = newUserDto.getLastName();
        String role = newUserDto.getRole();
        MultipartFile multipartFile = newUserDto.getMultipartFile();
        var userRole = roleRepository.findRoleByName(role);

        User user = UserBuilder.build(firstName, lastName, username, passwordEncoder, defaultPassword, email, userRole);
        saveProfileImage(user, multipartFile);
        userRepository.save(user);
        return new ResponseEntity<>(newUserDto, OK);
    }

    private void saveProfileImage(User user, MultipartFile profileImage) throws IOException {
        if (profileImage != null) {
            Path userFolder = Paths.get(USER_FOLDER + user.getUsername()).toAbsolutePath().normalize();
            if (!Files.exists(userFolder)) {
                Files.createDirectories(userFolder);
                log.info(DIRECTORY_CREATED + user.getUsername());
            }
            Files.deleteIfExists(Paths.get(USER_FOLDER + user.getUsername() + DOT + JPG_EXTENSION));
            Files.copy(profileImage.getInputStream(), userFolder.resolve(user.getUsername() + DOT + JPG_EXTENSION), REPLACE_EXISTING);
            user.setProfileImgUrl(setProfileImageUrl(user.getUsername()));
            log.info(FILE_SAVED_IN_THE_FILE_SYSTEM + profileImage.getOriginalFilename());
        }
    }

    private String setProfileImageUrl(String username) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath().path(USER_IMAGE_PATH + username + FORWARD_SLASH + username + DOT + JPG_EXTENSION).toUriString();
    }

    public ResponseEntity<UpdateUserDto> updateUser(UpdateUserDto updateUserDto) throws UserExistsException, IOException {
        String newEmail = updateUserDto.getEmail();
        String currentUsername = updateUserDto.getCurrentUsername();
        if (StringUtils.isNotBlank(newEmail)) {
            validateEmailCredentials(newEmail);
        }

        User user = userRepository.findByUsername(currentUsername).orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));

        String newFirstName = updateUserDto.getFirstName();
        String newLastName = updateUserDto.getLastName();
        String role = updateUserDto.getRole();
        Boolean isNonLocked = updateUserDto.getIsNonLocked();
        Boolean isActive = updateUserDto.getIsActive();
        MultipartFile multipartFile = updateUserDto.getMultipartFile();

        user.setFirstName(newFirstName);
        user.setLastName(newLastName);
        if (StringUtils.isNotBlank(newEmail)) {
            user.setEmail(newEmail);
        }
        var userRole = this.roleRepository.findRoleByName(getRole(role).name());
        user.setRole(userRole);
        user.setIsNonLocked(isNonLocked);
        user.setIsActive(isActive);
        saveProfileImage(user, multipartFile);
        userRepository.save(user);

        return new ResponseEntity<>(updateUserDto, OK);
    }

    private Role getRole(String role) {
        return Role.valueOf(role.toUpperCase(Locale.ROOT));
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public User updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, IOException {
        validateUsernameCredentials(username);
        User user = userRepository.findByUsername(username).orElse(null);
        saveProfileImage(user, profileImage);
        return user;
    }

    public ResponseEntity<UserRegisterDto> register(UserRegisterDto userRegisterDto) throws UserExistsException {
        String firstName = userRegisterDto.getFirstName();
        String lastName = userRegisterDto.getLastName();
        String username = userRegisterDto.getUsername();
        String password = userRegisterDto.getPassword();
        String email = userRegisterDto.getEmail();

        validateRegisterCredentials(username, email);

        var userRole = this.roleRepository.findRoleByName(USER.name());
        User user = UserBuilder.build(firstName, lastName, username, passwordEncoder, password, email, userRole);


        userRepository.save(user);
        log.info(String.format(USER_LOGGER_REGISTER_INFO, username));
        return new ResponseEntity<>(userRegisterDto, OK);
    }

    public ResponseEntity<UserLoginDto> login(UserLoginDto userLoginDto) {
        String username = userLoginDto.getUsername();
        String password = userLoginDto.getPassword();
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContext newContext = SecurityContextHolder.createEmptyContext();
        newContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(newContext);

        HttpHeaders jwtHeader = getJwtHeader();
        return new ResponseEntity<>(userLoginDto, jwtHeader, OK);
    }

    private HttpHeaders getJwtHeader() {
        HttpHeaders httpHeader = new HttpHeaders();
        httpHeader.add(JWT_TOKEN_HEADER, jwtProvider.generateJwt());
        return httpHeader;
    }

    private void validateRegisterCredentials(String username, String email) throws UserExistsException {
        Optional<User> optionalUser = this.userRepository.findByUsernameOrEmail(username, email);
        if (optionalUser.isPresent()) {
            throw new UserExistsException(ExceptionMessages.USERNAME_EXISTS);
        }
    }

    private void validateUsernameCredentials(String username) throws UserNotFoundException {
        Optional<User> optionalUser = this.userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(USER_BY_USERNAME_NOT_FOUND);
        }
    }

    private void validateEmailCredentials(String email) throws UserExistsException {
        Optional<User> optionalUser = this.userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            throw new UserExistsException(ExceptionMessages.EMAIL_EXISTS);
        }
    }

    public ResponseEntity<User> findUserByUsername(String username) throws UserNotFoundException {
        Optional<User> optionalUser = this.userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(USER_BY_USERNAME_NOT_FOUND);
        }

        return new ResponseEntity<>(optionalUser.get(), OK);
    }

    public ResponseEntity<List<User>> findAllUsers() {
        List<User> users = this.userRepository.findAll();
        return new ResponseEntity<>(users, OK);
    }

    private static class UserBuilder {
        private static User build(String firstName, String lastName, String username, PasswordEncoder passwordEncoder,
                                  String password, String email, bg.softuni.jwt.model.Role userRole) {

            return User.builder()
                    .userId(generateUserId())
                    .firstName(firstName)
                    .lastName(lastName)
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .email(email)
                    .joinDate(new Date())
                    .profileImgUrl(constructProfileImageUrl(username))
                    .role(userRole)
                    .isNonLocked(true).isActive(true).build();
        }
    }

    private static String generateUserId() {
        return RandomStringUtils.randomAscii(10).replaceAll("\s", "");
    }

    private static String constructProfileImageUrl(String username) {
        final String DEFAULT_USER_IMAGE_PATH = "/user/image/profile/%s";
        return ServletUriComponentsBuilder
                .fromCurrentContextPath().path(String.format(DEFAULT_USER_IMAGE_PATH, username)).toUriString();
    }
}