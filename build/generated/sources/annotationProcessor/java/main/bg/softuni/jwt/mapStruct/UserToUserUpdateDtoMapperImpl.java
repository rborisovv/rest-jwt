package bg.softuni.jwt.mapStruct;

import bg.softuni.jwt.dto.UserUpdateDto;
import bg.softuni.jwt.model.Role;
import bg.softuni.jwt.model.User;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-09-06T10:49:44+0300",
    comments = "version: 1.5.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.5.jar, environment: Java 17.0.2 (Oracle Corporation)"
)
public class UserToUserUpdateDtoMapperImpl implements UserToUserUpdateDtoMapper {

    @Override
    public UserUpdateDto userUpdateDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserUpdateDto.UserUpdateDtoBuilder userUpdateDto = UserUpdateDto.builder();

        userUpdateDto.role( userRoleName( user ) );
        userUpdateDto.profileImageUrl( user.getProfileImgUrl() );
        userUpdateDto.username( user.getUsername() );
        userUpdateDto.firstName( user.getFirstName() );
        userUpdateDto.lastName( user.getLastName() );
        userUpdateDto.email( user.getEmail() );
        userUpdateDto.joinDate( user.getJoinDate() );
        userUpdateDto.lastLoginDate( user.getLastLoginDate() );
        userUpdateDto.isNonLocked( user.getIsNonLocked() );
        userUpdateDto.isActive( user.getIsActive() );

        return userUpdateDto.build();
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
