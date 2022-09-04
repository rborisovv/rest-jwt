package bg.softuni.jwt.mapStruct;

import bg.softuni.jwt.dto.UserLoginDto;
import bg.softuni.jwt.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserToUserLoginDtoMapper {

    UserToUserLoginDtoMapper INSTANCE = Mappers.getMapper(UserToUserLoginDtoMapper.class);

    @Mapping(source = "role.name", target = "role")
    @Mapping(source = "profileImgUrl", target = "profileImageUrl")
    UserLoginDto userLoginDto(User user);
}