package org.habitApp.mappers;

import org.habitApp.domain.dto.userDto.UserDto;
import org.habitApp.domain.dto.userDto.UserDtoRegisterUpdate;
import org.habitApp.domain.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    //// UserDto
    /**
     * User to UserDto
     * @param user userEntity
     * @return UserDto
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "flagAdmin", target = "flagAdmin")
    UserDto userToUserDto(UserEntity user);

    /**
     * UserDto to User
     * @param userDto userDto
     * @return UserEntity
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "flagAdmin", target = "flagAdmin")
    UserEntity userDtoToUser(UserDto userDto);

    //UserDtoRegister
    /**
     * User to UserDtoRegister
     * @param user userEntity
     * @return UserDtoRegister
     */
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    UserDtoRegisterUpdate userToUserDtoRegisterUpdate(UserEntity user);

    /**
     * UserDtoRegister to User
     * @param userDtoRegisterUpdate userDtoRegister
     * @return UserEntity
     */
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    UserEntity userDtoRegisterUpdateToUser(UserDtoRegisterUpdate userDtoRegisterUpdate);
}

