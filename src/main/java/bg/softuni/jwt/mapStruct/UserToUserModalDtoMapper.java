package bg.softuni.jwt.mapStruct;

import bg.softuni.jwt.dto.UserModalDto;
import bg.softuni.jwt.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserToUserModalDtoMapper {
    UserToUserModalDtoMapper INSTANCE = Mappers.getMapper(UserToUserModalDtoMapper.class);

    @Mapping(source = "role.name", target = "role")
    @Mapping(source = "profileImgUrl", target = "profileImageUrl")
    @Mapping(source = "role.authorities", target = "authorities")
    UserModalDto createUserModalDto(User user);
}