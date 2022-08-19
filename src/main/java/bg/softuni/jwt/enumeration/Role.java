package bg.softuni.jwt.enumeration;

import java.util.Set;

import static bg.softuni.jwt.common.Authority.*;

public enum Role {
    USER(USER_AUTHORITIES),
    HR(HR_AUTHORITIES),
    MANAGER(MANAGER_AUTHORITIES),
    ADMIN(ADMIN_AUTHORITIES),
    MODERATOR(MODERATOR_AUTHORITIES);


    private final Set<String> authorities;

    Role(Set<String> authorities) {
        this.authorities = authorities;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }
}