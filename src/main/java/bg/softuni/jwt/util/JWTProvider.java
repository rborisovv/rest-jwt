package bg.softuni.jwt.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import static bg.softuni.jwt.common.SecurityConstant.*;
import static java.util.Arrays.stream;

@Component
public class JWTProvider {
    private final UserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String secret;

    public JWTProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public String generateJwt() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String[] claims = getClaimsFromUser(username);
        return JWT.create()
                .withIssuer(TOKEN_ISSUER)
                .withAudience(ISSUER_ADMINISTRATION)
                .withIssuedAt(new Date())
                .withSubject(username)
                .withArrayClaim(AUTHORITIES, claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(secret.getBytes(StandardCharsets.UTF_8)));
    }

    public Set<GrantedAuthority> getAuthorities(String token) {
        String[] claims = getClaimsFromToken(token);
        return stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    public Authentication getAuthentication(String username, Set<GrantedAuthority> grantedAuthorities, HttpServletRequest httpServletRequest) {
        UsernamePasswordAuthenticationToken userPasswordToken = new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
        userPasswordToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
        return userPasswordToken;
    }

    public String getSubject(String token) {
        JWTVerifier jwtVerifier = getJWTVerifier();
        return jwtVerifier.verify(token).getSubject();
    }

    public boolean isTokenValid(String username, String token) {
        JWTVerifier verifier = getJWTVerifier();
        return StringUtils.isNotBlank(username) && !isTokenExpired(verifier, token);
    }

    private boolean isTokenExpired(JWTVerifier verifier, String token) {
        Date expiration = verifier.verify(token).getExpiresAt();
        return expiration.before(new Date());
    }

    private String[] getClaimsFromToken(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
    }

    private JWTVerifier getJWTVerifier() {
        JWTVerifier jwtVerifier;

        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);
            jwtVerifier = JWT.require(algorithm).withIssuer(TOKEN_ISSUER).build();
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException(TOKEN_CANNOT_BE_VERIFIED);
        }

        return jwtVerifier;
    }

    private String[] getClaimsFromUser(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return userDetails.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);
    }
}