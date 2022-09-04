package bg.softuni.jwt.mapStruct;

import bg.softuni.jwt.dto.UsersDto;
import bg.softuni.jwt.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserToUsersDtoMapper {

    UserToUsersDtoMapper INSTANCE = Mappers.getMapper(UserToUsersDtoMapper.class);

    @Mapping(source = "role.name", target = "role")
    @Mapping(source = "profileImgUrl", target = "profileImageUrl")
    @Mapping(source = "role.authorities", target = "authorities")
    UsersDto usersDto(User user);
}