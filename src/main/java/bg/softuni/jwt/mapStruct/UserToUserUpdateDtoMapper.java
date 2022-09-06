package bg.softuni.jwt.mapStruct;

import bg.softuni.jwt.dto.UserUpdateDto;
import bg.softuni.jwt.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserToUserUpdateDtoMapper {

    UserToUserUpdateDtoMapper INSTANCE = Mappers.getMapper(UserToUserUpdateDtoMapper.class);

    @Mapping(source = "role.name", target = "role")
    @Mapping(source = "profileImgUrl", target = "profileImageUrl")
    UserUpdateDto userUpdateDto(User user);
}