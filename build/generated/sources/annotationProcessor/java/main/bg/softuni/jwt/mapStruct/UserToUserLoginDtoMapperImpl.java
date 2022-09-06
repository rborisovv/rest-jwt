package bg.softuni.jwt.mapStruct;

import bg.softuni.jwt.dto.UserLoginDto;
import bg.softuni.jwt.model.Role;
import bg.softuni.jwt.model.User;
import java.util.Date;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-09-06T10:49:44+0300",
    comments = "version: 1.5.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.5.jar, environment: Java 17.0.2 (Oracle Corporation)"
)
public class UserToUserLoginDtoMapperImpl implements UserToUserLoginDtoMapper {

    @Override
    public UserLoginDto userLoginDto(User user) {
        if ( user == null ) {
            return null;
        }

        String role = null;
        String profileImageUrl = null;
        String firstName = null;
        String lastName = null;
        String email = null;
        Boolean isActive = null;
        Boolean isNonLocked = null;
        Date joinDate = null;
        Date lastLoginDate = null;
        String username = null;
        String password = null;

        role = userRoleName( user );
        profileImageUrl = user.getProfileImgUrl();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
        isActive = user.getIsActive();
        isNonLocked = user.getIsNonLocked();
        joinDate = user.getJoinDate();
        lastLoginDate = user.getLastLoginDate();
        username = user.getUsername();
        password = user.getPassword();

        UserLoginDto userLoginDto = new UserLoginDto( firstName, lastName, email, isActive, isNonLocked, role, profileImageUrl, joinDate, lastLoginDate, username, password );

        return userLoginDto;
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
}
