package com.excelsisproject.productservice.service.implementation;

import com.excelsisproject.productservice.dto.UserDto;
import com.excelsisproject.productservice.entity.User;
import com.excelsisproject.productservice.exception.ResourceNotFoundException;
import com.excelsisproject.productservice.mapper.UserMapper;
import com.excelsisproject.productservice.repository.UserRepository;
import com.excelsisproject.productservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.mapToUsuario(userDto);
        User savedUser = userRepository.save(user);
        return UserMapper.mapToUserDto(savedUser);
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("Product does not exists with given id: " + userId));

        return UserMapper.mapToUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map((usuario) -> UserMapper.mapToUserDto(usuario))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(Long userId, UserDto updatedUser) {
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new ResourceNotFoundException("User does not exist with given id: " + userId));

        user.setName(updatedUser.getName());
        user.setLastName(updatedUser.getLastName());
        user.setUserName(updatedUser.getUserName());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());

        User updatedUserObj = userRepository.save(user);

        return UserMapper.mapToUserDto(updatedUserObj);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new ResourceNotFoundException("User does not exist with given id: " + userId));

        userRepository.deleteById(userId);

    }

}
