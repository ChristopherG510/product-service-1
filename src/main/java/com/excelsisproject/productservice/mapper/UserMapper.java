package com.excelsisproject.productservice.mapper;

import com.excelsisproject.productservice.dto.UserDto;
import com.excelsisproject.productservice.entity.User;

public class UserMapper {
    public static UserDto mapToUserDto(User user){
        return new UserDto(
            user.getId(),
            user.getName(),
            user.getLastName(),
            user.getUserName(),
            user.getEmail(),
            user.getPassword()
        );
    }

    public static User mapToUsuario(UserDto userDto){
        return new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getLastName(),
                userDto.getUserName(),
                userDto.getEmail(),
                userDto.getPassword()
        );

    }
}
