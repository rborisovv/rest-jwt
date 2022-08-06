package bg.softuni.jwt.service;

import bg.softuni.jwt.dao.UserRepository;
import bg.softuni.jwt.enumeration.Authority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static bg.softuni.jwt.common.Messages.userNotFoundMessage;

public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(this::mapUser)
                .map(this::userDetails)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(userNotFoundMessage, username)));
    }

    private bg.softuni.jwt.model.User mapUser(bg.softuni.jwt.model.User user) {
        user.setGetLastLoginDateDisplay(user.getLastLoginDate());
        user.setLastLoginDate(new Date());
        return user;
    }

    private UserDetails userDetails(bg.softuni.jwt.model.User user) {
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(this.map(user.getAuthorities()))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    private Collection<? extends GrantedAuthority> map(Set<Authority> userAuthorities) {
        Function<Authority, GrantedAuthority> mapAuthority = authority -> new SimpleGrantedAuthority(authority.name().toUpperCase(Locale.ROOT));
        return userAuthorities.stream().map(mapAuthority).collect(Collectors.toSet());
    }
}