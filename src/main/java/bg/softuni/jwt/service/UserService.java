package bg.softuni.jwt.service;

import bg.softuni.jwt.common.ExceptionMessages;
import bg.softuni.jwt.dao.UserRepository;
import bg.softuni.jwt.exception.UsernameExistsException;
import bg.softuni.jwt.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static bg.softuni.jwt.enumeration.Role.ROLE_USER;

@Service
@Slf4j
public class UserService {
    private static final String USER_LOGGER_REGISTER_INFO = "User with %s username successfully registered!";
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String firstName, String lastName, String username, String password, String email) throws UsernameExistsException {
        validateNewCredentials(firstName, lastName);
        User user = UserBuilder.build(firstName, lastName, username, passwordEncoder, password, email);
        User savedUser = userRepository.save(user);
        log.info(String.format(USER_LOGGER_REGISTER_INFO, username));
        return savedUser;
    }

    public List<User> getUsers() {
        return null;
    }

    private void validateNewCredentials(String username, String email) throws UsernameExistsException {
        Optional<User> optionalUser = this.userRepository.findByUsernameOrEmail(username, email);
        if (optionalUser.isPresent()) {
            throw new UsernameExistsException(ExceptionMessages.USERNAME_EXISTS);
        }
    }

    private static class UserBuilder {
        private static User build(String firstName, String lastName, String username, PasswordEncoder passwordEncoder, String password, String email) {
            String imagePath = ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/image/profile/temp").toUriString();
            return new User(
                    RandomStringUtils.randomAscii(10).replaceAll("\s", ""),
                    firstName, lastName, username, passwordEncoder.encode(password),
                    email, imagePath, new Date(), ROLE_USER.name(), ROLE_USER.getAuthorities(),
                    true, true
            );
        }
    }
}