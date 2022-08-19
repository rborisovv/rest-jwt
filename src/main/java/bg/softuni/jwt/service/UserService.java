package bg.softuni.jwt.service;

import bg.softuni.jwt.common.ExceptionMessages;
import bg.softuni.jwt.dao.UserRepository;
import bg.softuni.jwt.dto.UserLoginDto;
import bg.softuni.jwt.dto.UserRegisterDto;
import bg.softuni.jwt.exception.UsernameExistsException;
import bg.softuni.jwt.model.User;
import bg.softuni.jwt.util.JWTProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Date;
import java.util.Optional;

import static bg.softuni.jwt.common.SecurityConstant.JWT_TOKEN_HEADER;
import static bg.softuni.jwt.enumeration.Role.USER;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Service
@Qualifier("UserService")
public class UserService {
    private static final String USER_LOGGER_REGISTER_INFO = "User with %s username successfully registered!";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JWTProvider jwtProvider;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService, AuthenticationManager authenticationManager, JWTProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    public UserRegisterDto register(UserRegisterDto userRegisterDto) throws UsernameExistsException {
        String firstName = userRegisterDto.getFirstName();
        String lastName = userRegisterDto.getLastName();
        String username = userRegisterDto.getUsername();
        String password = userRegisterDto.getPassword();
        String email = userRegisterDto.getEmail();

        validateRegisterCredentials(firstName, lastName);
        User user = UserBuilder.build(firstName, lastName, username, passwordEncoder, password, email);
        userRepository.save(user);
        log.info(String.format(USER_LOGGER_REGISTER_INFO, username));
        return userRegisterDto;
    }

    public ResponseEntity<UserLoginDto> login(UserLoginDto userLoginDto) {
        String username = userLoginDto.getUsername();
        String password = userLoginDto.getPassword();
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authToken);

        HttpHeaders jwtHeader = getJwtHeader();
        return new ResponseEntity<>(userLoginDto, jwtHeader, OK);
    }

    private HttpHeaders getJwtHeader() {
        HttpHeaders httpHeader = new HttpHeaders();
        httpHeader.add(JWT_TOKEN_HEADER, jwtProvider.generateJwt());
        return httpHeader;
    }

    private void validateRegisterCredentials(String username, String email) throws UsernameExistsException {
        Optional<User> optionalUser = this.userRepository.findByUsernameOrEmail(username, email);
        if (optionalUser.isPresent()) {
            throw new UsernameExistsException(ExceptionMessages.USERNAME_EXISTS);
        }
    }

    private static class UserBuilder {
        private static final String DEFAULT_USER_IMAGE_PATH = "/user/image/profile/temp";
        private static final String IMAGE_PATH = ServletUriComponentsBuilder.fromCurrentContextPath().path(DEFAULT_USER_IMAGE_PATH).toUriString();

        private static User build(String firstName, String lastName, String username, PasswordEncoder passwordEncoder, String password, String email) {
            return new User(RandomStringUtils.randomAscii(10).replaceAll("\s", ""),
                    firstName, lastName, username, passwordEncoder.encode(password),
                    email, IMAGE_PATH, new Date(), USER.name(), USER.getAuthorities(),
                    true, true
            );
        }
    }
}