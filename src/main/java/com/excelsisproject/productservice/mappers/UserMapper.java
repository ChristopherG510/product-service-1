package com.excelsisproject.productservice.mappers;


import com.excelsisproject.productservice.dto.SignUpDto;
import com.excelsisproject.productservice.dto.UserDto;
import com.excelsisproject.productservice.entities.ERole;
import com.excelsisproject.productservice.entities.RoleEntity;
import com.excelsisproject.productservice.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    public static UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        } else {
            UserDto.UserDtoBuilder userDto = UserDto.builder();
            userDto.id(user.getId());
            userDto.firstName(user.getFirstName());
            userDto.lastName(user.getLastName());
            userDto.login(user.getLogin());
            userDto.roles(user.getRoles().stream().map(role -> role.getName().toString()).collect(Collectors.toSet()));
            return userDto.build();
        }
    }

    @Mapping(target = "password", ignore = true)
    public static User signUpToUser(SignUpDto signUpDto) {
        if (signUpDto == null) {
            return null;
        } else {
            Set<RoleEntity> roles = signUpDto.roles().stream().map(role->RoleEntity.builder().name(ERole.valueOf(role)).build()).collect(Collectors.toSet());
            User.UserBuilder user = User.builder();
            user.firstName(signUpDto.firstName());
            user.lastName(signUpDto.lastName());
            user.login(signUpDto.login());
            user.roles(roles);
            return user.build();
        }
    }
}
