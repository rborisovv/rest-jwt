package bg.softuni.jwt.init;

import bg.softuni.jwt.common.Authority;
import bg.softuni.jwt.dao.RoleRepository;
import bg.softuni.jwt.model.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

import static bg.softuni.jwt.enumeration.Role.OWNER;

@Component
public class DatabaseInit implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DatabaseInit(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (roleRepository.count() == 0) {
            Role userRole = Role.builder()
                    .name(bg.softuni.jwt.enumeration.Role.USER.name())
                    .authorities(Authority.USER_AUTHORITIES)
                    .build();

            Role hrRole = Role.builder()
                    .name(bg.softuni.jwt.enumeration.Role.HR.name())
                    .authorities(Authority.HR_AUTHORITIES)
                    .build();

            Role managerRole = Role.builder()
                    .name(bg.softuni.jwt.enumeration.Role.MANAGER.name())
                    .authorities(Authority.MANAGER_AUTHORITIES)
                    .build();

            Role adminRole = Role.builder()
                    .name(bg.softuni.jwt.enumeration.Role.ADMIN.name())
                    .authorities(Authority.ADMIN_AUTHORITIES)
                    .build();

            Role moderatorRole = Role.builder()
                    .name(OWNER.name())
                    .authorities(Authority.MODERATOR_AUTHORITIES)
                    .build();

            roleRepository.saveAll(Set.of(userRole, hrRole, managerRole, adminRole, moderatorRole));
        }
    }
}