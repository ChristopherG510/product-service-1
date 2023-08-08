package com.excelsisproject.productservice.services;



import com.excelsisproject.productservice.config.SecurityConfig;
import com.excelsisproject.productservice.dto.CredentialsDto;
import com.excelsisproject.productservice.dto.SignUpDto;
import com.excelsisproject.productservice.dto.UserDto;
import com.excelsisproject.productservice.entities.User;
import com.excelsisproject.productservice.exceptions.AppException;
import com.excelsisproject.productservice.mappers.UserMapper;
import com.excelsisproject.productservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    private SecurityConfig securityConfig;

    public UserDto login(CredentialsDto credentialsDto){
        User user = userRepository.findByLogin(credentialsDto.getLogin())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        if (securityConfig.passwordEncoder().matches(CharBuffer.wrap(credentialsDto.getPassword()), user.getPassword())) {
            return UserMapper.toUserDto(user);
        }
        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public UserDto register(SignUpDto signUpDto) {
        Optional<User> optionalUser = userRepository.findByLogin(signUpDto.login());

        if (optionalUser.isPresent()){
            throw new AppException("Login already exists", HttpStatus.BAD_REQUEST);
        }
        User user = UserMapper.signUpToUser(signUpDto);
        user.setPassword(securityConfig.passwordEncoder().encode(CharBuffer.wrap(signUpDto.password())));
        User savedUser = userRepository.save(user);
        return UserMapper.toUserDto(savedUser);
    }

    public Long getLoggedUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUser = authentication.getName();

        User user = userRepository.findByLogin(loggedUser)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario" + loggedUser + "no existe"));
        Long loggedUserId = user.getId();

        return loggedUserId;
    }

}