package bg.softuni.jwt.configuration;

import bg.softuni.jwt.dao.UserRepository;
import bg.softuni.jwt.httpFilter.JWTAccessDeniedHandler;
import bg.softuni.jwt.httpFilter.JWTAuthEntryPoint;
import bg.softuni.jwt.httpFilter.JWTAuthFilter;
import bg.softuni.jwt.service.AppUserDetailsService;
import bg.softuni.jwt.service.LoginAttemptService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static bg.softuni.jwt.common.SecurityConstant.PUBLIC_URLS;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {
    private final JWTAccessDeniedHandler accessDeniedHandler;
    private final JWTAuthEntryPoint jwtAuthEntryPoint;
    private final JWTAuthFilter jwtAuthFilter;
    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;

    public SecurityConfiguration(JWTAccessDeniedHandler accessDeniedHandler, JWTAuthEntryPoint jwtAuthEntryPoint, JWTAuthFilter jwtAuthFilter, UserRepository userRepository, LoginAttemptService loginAttemptService) {
        this.accessDeniedHandler = accessDeniedHandler;
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
        this.jwtAuthFilter = jwtAuthFilter;
        this.userRepository = userRepository;
        this.loginAttemptService = loginAttemptService;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new AppUserDetailsService(userRepository, loginAttemptService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .cors()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .permitAll()
                .antMatchers(PUBLIC_URLS)
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(jwtAuthEntryPoint)
                .and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}