package bg.softuni.jwt.service;

import bg.softuni.jwt.dao.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static bg.softuni.jwt.common.ExceptionMessages.USER_NOT_FOUND;

public class AppUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;

    public AppUserDetailsService(UserRepository userRepository, LoginAttemptService loginAttemptService) {
        this.userRepository = userRepository;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        bg.softuni.jwt.model.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
        validateLoginAttempt(user);
        userRepository.save(user);
        return Optional.of(user).map(this::userDetails).get();
    }

    private UserDetails userDetails(bg.softuni.jwt.model.User user) {
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .authorities(this.map(user.getAuthorities()))
                .accountExpired(false)
                .accountLocked(!user.getIsNotLocked())
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    private void validateLoginAttempt(bg.softuni.jwt.model.User user) {
        if (user.getIsNotLocked()) {
            user.setIsNotLocked(!loginAttemptService.hasExceededMaxAttempts(user.getUsername()));
        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        }
    }

    private Collection<? extends GrantedAuthority> map(Set<String> userAuthorities) {
        Function<String, GrantedAuthority> mapAuthority =
                authority -> new SimpleGrantedAuthority(authority.toUpperCase(Locale.ROOT));
        return userAuthorities.stream().map(mapAuthority).collect(Collectors.toSet());
    }
}