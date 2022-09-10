package bg.softuni.jwt.common;

import java.util.Set;

import static bg.softuni.jwt.enumeration.Authority.*;

public class Authority {
    public static final Set<String> USER_AUTHORITIES = Set.of(READ.name());

    public static final Set<String> HR_AUTHORITIES = Set.of(READ.name(), UPDATE.name(), CREATE.name());

    public static final Set<String> MANAGER_AUTHORITIES = Set.of(READ.name(), UPDATE.name());

    public static final Set<String> ADMIN_AUTHORITIES = Set.of(READ.name(), CREATE.name(), UPDATE.name());

    public static final Set<String> OWNER_AUTHORITIES = Set.of(READ.name(), CREATE.name(), UPDATE.name(), DELETE.name());
}