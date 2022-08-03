package bg.softuni.jwt.configuration;

import bg.softuni.jwt.dao.UserRepository;
import bg.softuni.jwt.service.AppUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY;
import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;

@Configuration
public class SecurityConfiguration {

    @Bean
    public UserDetailsService userPrincipal(UserRepository userRepository) {
        return new AppUserDetailsService(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests()
                .antMatchers("/login", "/register")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter(SPRING_SECURITY_FORM_USERNAME_KEY)
                .passwordParameter(SPRING_SECURITY_FORM_PASSWORD_KEY)
                .successForwardUrl("/")
                .and()
                .logout()
                .clearAuthentication(true)
                .and().build();
    }
}