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
                .map(this::userDetails)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(userNotFoundMessage, username)));
    }

    private UserDetails userDetails(bg.softuni.jwt.model.User user) {
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .authorities(this.map(user.getAuthorities()))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    private Collection<? extends GrantedAuthority> map(Set<String> userAuthorities) {
        Function<String, GrantedAuthority> mapAuthority = authority -> new SimpleGrantedAuthority(authority.toUpperCase(Locale.ROOT));
        return userAuthorities.stream().map(mapAuthority).collect(Collectors.toSet());
    }
}