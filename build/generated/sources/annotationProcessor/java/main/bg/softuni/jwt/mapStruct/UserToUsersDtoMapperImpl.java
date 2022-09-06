package bg.softuni.jwt.mapStruct;

import bg.softuni.jwt.dto.UsersDto;
import bg.softuni.jwt.model.Role;
import bg.softuni.jwt.model.User;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-09-06T10:49:43+0300",
    comments = "version: 1.5.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.5.jar, environment: Java 17.0.2 (Oracle Corporation)"
)
public class UserToUsersDtoMapperImpl implements UserToUsersDtoMapper {

    @Override
    public UsersDto usersDto(User user) {
        if ( user == null ) {
            return null;
        }

        String role = null;
        String profileImageUrl = null;
        Set<String> authorities = null;
        String firstName = null;
        String lastName = null;
        String username = null;
        String email = null;
        Date joinDate = null;
        Date lastLoginDate = null;
        Boolean isNonLocked = null;
        Boolean isActive = null;

        role = userRoleName( user );
        profileImageUrl = user.getProfileImgUrl();
        Set<String> authorities1 = userRoleAuthorities( user );
        Set<String> set = authorities1;
        if ( set != null ) {
            authorities = new LinkedHashSet<String>( set );
        }
        firstName = user.getFirstName();
        lastName = user.getLastName();
        username = user.getUsername();
        email = user.getEmail();
        joinDate = user.getJoinDate();
        lastLoginDate = user.getLastLoginDate();
        isNonLocked = user.getIsNonLocked();
        isActive = user.getIsActive();

        UsersDto usersDto = new UsersDto( firstName, lastName, username, email, role, authorities, joinDate, lastLoginDate, isNonLocked, isActive, profileImageUrl );

        return usersDto;
    }

    private String userRoleName(User user) {
        if ( user == null ) {
            return null;
        }
        Role role = user.getRole();
        if ( role == null ) {
            return null;
        }
        String name = role.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private Set<String> userRoleAuthorities(User user) {
        if ( user == null ) {
            return null;
        }
        Role role = user.getRole();
        if ( role == null ) {
            return null;
        }
        Set<String> authorities = role.getAuthorities();
        if ( authorities == null ) {
            return null;
        }
        return authorities;
    }
}
