package com.excelsisproject.productservice.service;

import com.excelsisproject.productservice.dto.ProductDto;
import com.excelsisproject.productservice.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUserById(Long userId); // Obtener un usuario por id

    List<UserDto> getAllUsers(); // Obtener todos los usuarios

    UserDto updateUser(Long userId, UserDto updatedUser); // Modificar un usuario

    void deleteUser(Long userId); // Eliminar un usuario
}
