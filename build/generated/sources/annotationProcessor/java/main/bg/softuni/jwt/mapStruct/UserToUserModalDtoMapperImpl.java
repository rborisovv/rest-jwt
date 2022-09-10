package bg.softuni.jwt.mapStruct;

import bg.softuni.jwt.dto.UserModalDto;
import bg.softuni.jwt.enumeration.Authority;
import bg.softuni.jwt.model.Role;
import bg.softuni.jwt.model.User;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-09-10T16:53:08+0300",
    comments = "version: 1.5.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.5.jar, environment: Java 17.0.2 (Oracle Corporation)"
)
public class UserToUserModalDtoMapperImpl implements UserToUserModalDtoMapper {

    @Override
    public UserModalDto createUserModalDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserModalDto.UserModalDtoBuilder userModalDto = UserModalDto.builder();

        userModalDto.role( userRoleName( user ) );
        userModalDto.profileImageUrl( user.getProfileImgUrl() );
        Set<String> authorities = userRoleAuthorities( user );
        userModalDto.authorities( stringSetToAuthoritySet( authorities ) );
        userModalDto.username( user.getUsername() );
        userModalDto.firstName( user.getFirstName() );
        userModalDto.lastName( user.getLastName() );
        userModalDto.email( user.getEmail() );
        userModalDto.joinDate( user.getJoinDate() );
        userModalDto.lastLoginDate( user.getLastLoginDate() );
        userModalDto.isNonLocked( user.getIsNonLocked() );
        userModalDto.isActive( user.getIsActive() );

        return userModalDto.build();
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

    protected Set<Authority> stringSetToAuthoritySet(Set<String> set) {
        if ( set == null ) {
            return null;
        }

        Set<Authority> set1 = new LinkedHashSet<Authority>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( String string : set ) {
            set1.add( Enum.valueOf( Authority.class, string ) );
        }

        return set1;
    }
}
