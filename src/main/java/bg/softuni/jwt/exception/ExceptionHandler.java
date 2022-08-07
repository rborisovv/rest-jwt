package bg.softuni.jwt.exception;

import bg.softuni.jwt.domain.HTTPResponse;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.NoResultException;
import javax.security.auth.login.AccountLockedException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Locale;
import java.util.Objects;

import static bg.softuni.jwt.common.ExceptionMessages.*;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(DisabledException.class)
    public ResponseEntity<HTTPResponse> accountDisabledException() {
        return createHttpResponse(HttpStatus.BAD_REQUEST, ACCOUNT_DISABLED);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HTTPResponse> badCredentialsException() {
        return createHttpResponse(HttpStatus.BAD_REQUEST, INCORRECT_CREDENTIALS);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HTTPResponse> accessDeniedException() {
        return createHttpResponse(HttpStatus.FORBIDDEN, NOT_ENOUGH_PERMISSION);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<HTTPResponse> accountLockedException() {
        return createHttpResponse(HttpStatus.UNAUTHORIZED, ACCOUNT_LOCKED);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<HTTPResponse> tokenExpiredException(TokenExpiredException exception) {
        return createHttpResponse(HttpStatus.UNAUTHORIZED, exception.getMessage().toUpperCase(Locale.ROOT));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<HTTPResponse> emailExistsException(EmailExistsException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<HTTPResponse> emailNotFoundException(EmailNotFoundException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UsernameExistsException.class)
    public ResponseEntity<HTTPResponse> usernameExistsException(UsernameNotFoundException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<HTTPResponse> userNotFoundException(UserNotFoundException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<HTTPResponse> methodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        HttpMethod supportedMethod = Objects.requireNonNull(exception.getSupportedHttpMethods()).iterator().next();
        return createHttpResponse(HttpStatus.METHOD_NOT_ALLOWED, String.format(METHOD_IS_NOT_ALLOWED, supportedMethod));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<HTTPResponse> interServerErrorException(Exception exception) {
        log.error(exception.getMessage());
        return createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NoResultException.class)
    public ResponseEntity<HTTPResponse> notFoundException(NoResultException exception) {
        log.error(exception.getMessage());
        return createHttpResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IOException.class)
    public ResponseEntity<HTTPResponse> ioException(IOException exception) {
        log.error(exception.getMessage());
        return createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_PROCESSING_FILE);
    }



    private ResponseEntity<HTTPResponse> createHttpResponse(HttpStatus httpStatus, String message) {
        HTTPResponse httpResponse = new HTTPResponse(httpStatus.value(),
                httpStatus, httpStatus.getReasonPhrase().toUpperCase(Locale.ROOT),
                message);

        return new ResponseEntity<>(httpResponse, httpStatus);
    }
}