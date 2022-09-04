package bg.softuni.jwt.mapStruct;

import bg.softuni.jwt.dto.UsersDto;
import bg.softuni.jwt.model.Role;
import bg.softuni.jwt.model.User;
import java.util.Date;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-09-04T19:39:55+0300",
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
        String firstName = null;
        String lastName = null;
        String email = null;
        Date joinDate = null;
        Boolean isNonLocked = null;
        Boolean isActive = null;

        role = userRoleName( user );
        profileImageUrl = user.getProfileImgUrl();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
        joinDate = user.getJoinDate();
        isNonLocked = user.getIsNonLocked();
        isActive = user.getIsActive();

        UsersDto usersDto = new UsersDto( firstName, lastName, email, role, joinDate, isNonLocked, isActive, profileImageUrl );

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
}
