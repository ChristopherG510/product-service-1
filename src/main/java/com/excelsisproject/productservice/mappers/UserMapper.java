package com.excelsisproject.productservice.mappers;


import com.excelsisproject.productservice.dto.SignUpDto;
import com.excelsisproject.productservice.dto.UserDto;
import com.excelsisproject.productservice.entities.Roles;
import com.excelsisproject.productservice.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {


    // Crear un userDto en base a un User
    public static UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        } else {
            UserDto.UserDtoBuilder userDto = UserDto.builder();
            userDto.id(user.getId());
            userDto.firstName(user.getFirstName());
            userDto.lastName(user.getLastName());
            userDto.userEmail(user.getUserEmail());
            userDto.userPhoneNumber(user.getUserPhoneNumber());
            userDto.userAddress(user.getUserAddress());
            userDto.login(user.getLogin());
            userDto.roles(user.getRoles().stream().map(Roles::getName).collect(Collectors.toSet()));
            return userDto.build();
        }
    }

    // Crear un user en base a un signUpDto
    @Mapping(target = "password", ignore = true)
    public static User signUpToUser(SignUpDto signUpDto) {
        if (signUpDto == null) {
            return null;
        } else {
            // Crear un nuevo objeto RoleEntity con el nombre ADMIN

            Roles role = new Roles();
            role.setName("CLIENT");

            // Crear un nuevo set de roles vacío
            Set<Roles> roles = new HashSet<>();

            // Añadir el rol admin al set
            roles.add(role);
            User.UserBuilder user = User.builder();
            user.firstName(signUpDto.firstName());
            user.lastName(signUpDto.lastName());
            user.userEmail(signUpDto.userEmail());
            user.userPhoneNumber(signUpDto.userPhoneNumber());
            user.userAddress(signUpDto.userAddress());
            user.login(signUpDto.login());
            user.roles(roles);
            return user.build();
        }
    }
}