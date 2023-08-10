package com.excelsisproject.productservice.services;



import com.excelsisproject.productservice.config.SecurityConfig;
import com.excelsisproject.productservice.dto.ConfirmationTokenDto;
import com.excelsisproject.productservice.dto.CredentialsDto;
import com.excelsisproject.productservice.dto.SignUpDto;
import com.excelsisproject.productservice.dto.UserDto;
import com.excelsisproject.productservice.entities.Roles;
import com.excelsisproject.productservice.entities.User;
import com.excelsisproject.productservice.exceptions.AppException;
import com.excelsisproject.productservice.exceptions.ResourceNotFoundException;
import com.excelsisproject.productservice.mappers.ConfirmationTokenMapper;
import com.excelsisproject.productservice.mappers.ProductMapper;
import com.excelsisproject.productservice.mappers.UserMapper;
import com.excelsisproject.productservice.repositories.ConfirmationTokenRepository;
import com.excelsisproject.productservice.repositories.RolesRepository;
import com.excelsisproject.productservice.repositories.UserRepository;
import com.excelsisproject.productservice.entities.ConfirmationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    private static final String TOKEN_EXPIRED = "EXPIRED";
    private static final String TOKEN_VERIFIED = "VERIFIED";
    private static final String TOKEN_SENT = "SENT";
    private static final String TOKEN_INVALID = "INVALID";
    //private static final String NEW_TOKEN_MSG = "<HTML><body> <a href=\"http://localhost:8080/newToken?token=" + token + "\">Su token ha expirado. Haga clic aqui para crear uno nuevo.</a></body></HTML>";

    @Autowired
    private ConfirmationTokenService confirmationTokenService;
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    private SecurityConfig securityConfig;

    public UserDto login(CredentialsDto credentialsDto){
        User user = userRepository.findByLogin(credentialsDto.getLogin())
                .orElseThrow(() -> new AppException("Usuario no existe", HttpStatus.NOT_FOUND));

        if (securityConfig.passwordEncoder().matches(CharBuffer.wrap(credentialsDto.getPassword()), user.getPassword())) {
            return UserMapper.toUserDto(user);
        }
        throw new AppException("Contraseña invalida", HttpStatus.BAD_REQUEST);
    }

    public UserDto register(SignUpDto signUpDto) {
        Optional<User> optionalUser = userRepository.findByLoginOrUserEmail(signUpDto.login(), signUpDto.userEmail());

        if (optionalUser.isPresent()){
            throw new AppException("El Usuario o Email ya esta registrado", HttpStatus.BAD_REQUEST);
        }

        User user = UserMapper.signUpToUser(signUpDto);
        user.setPassword(securityConfig.passwordEncoder().encode(CharBuffer.wrap(signUpDto.password())));
        User savedUser = userRepository.save(user);

        String confirmationToken = UUID.randomUUID().toString();
        ConfirmationTokenDto confirmationTokenDto = new ConfirmationTokenDto();
        confirmationTokenDto.setConfirmationToken(confirmationToken);
        confirmationTokenDto.setTimeCreated(LocalDateTime.now());
        confirmationTokenDto.setTimeExpired(LocalDateTime.now().plusMinutes(15));
        confirmationTokenDto.setTimeConfirmed(null);
        confirmationTokenDto.setUser(user);
        confirmationTokenDto.setStatus(TOKEN_SENT);
        confirmationTokenRepository.save(ConfirmationTokenMapper.mapToConfirmationToken(confirmationTokenDto));


        emailService.sendSimpleMailMessage(user.getFirstName(), user.getUserEmail(), confirmationToken);

        return UserMapper.toUserDto(savedUser);
    }

    public String confirmToken(String token){
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByConfirmationToken(token);
        User user = confirmationToken.getUser();

        if(LocalDateTime.now().isAfter(confirmationToken.getTimeExpired()) || (confirmationToken.getStatus() == TOKEN_EXPIRED)){
            confirmationToken.setStatus(TOKEN_EXPIRED);
            return "<HTML><body> <a href=\"http://localhost:8080/newToken?token=" + token + "\">Su token ha expirado. Haga clic aqui para crear uno nuevo.</a></body></HTML>";
        } else if (Objects.equals(confirmationToken.getStatus(), TOKEN_VERIFIED)) {
            return TOKEN_VERIFIED;
        } else if (Objects.equals(confirmationToken.getStatus(), TOKEN_SENT)){
            confirmationToken.setStatus(TOKEN_VERIFIED);
            Set<Roles> roles = user.getRoles();
            // Cambiar el rol PENDIENTE a CLIENTE
            for (Roles role : roles) {
                role.setName("ADMIN");
            }
            user.setRoles(roles);

            confirmationToken.setTimeConfirmed(LocalDateTime.now());
            confirmationTokenRepository.save(confirmationToken);
            userRepository.save(user);

            return "Usuario " + user.getLogin() + " Registrado";
        } else {
            return TOKEN_INVALID;
        }
    }

    public Long getLoggedUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUser = authentication.getName();

        User user = userRepository.findByLogin(loggedUser)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario" + loggedUser + "no existe"));
        Long loggedUserId = user.getId();

        return loggedUserId;
    }

    public List<UserDto> getAllUsers(){
        List<User> users = userRepository.findAll();

        return users.stream().map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public UserDto editMyUser(UserDto updatedUser){
        User user = userRepository.findById(getLoggedUserId()).stream().findFirst().orElseThrow(
                () -> new ResourceNotFoundException("User does not exists with given id: " + getLoggedUserId()));

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setUserEmail(updatedUser.getUserEmail());
        user.setUserPhoneNumber(updatedUser.getUserPhoneNumber());
        user.setLogin(updatedUser.getLogin());

        User updatedUserObj = userRepository.save(user);

        return UserMapper.toUserDto(updatedUserObj);
    }

    public UserDto editUserRoleOrStatus(UserDto updatedUser){
        User user = userRepository.findById(updatedUser.getId()).stream().findFirst().orElseThrow(
                () -> new ResourceNotFoundException("User does not exists with given id: " + updatedUser.getId()));
        // Roles roleObject = updatedUser.getRoles().stream().findFirst().get();


        Roles roleObject = user.getRoles().iterator().next();
        roleObject.setName(updatedUser.getRoles().iterator().next());
        String role = roleObject.getName();

        System.out.println("user: " + user.getLogin() + " id: " + user.getId());

        if(Objects.equals(role, "ADMIN") || Objects.equals(role, "CLIENTE") || Objects.equals(role, "BLOQUEADO")) {

            Set<Roles> roles = new HashSet<>();
            roles.add(roleObject);
            user.setRoles(roles);

            User savedUser = userRepository.save(user);
            return UserMapper.toUserDto(savedUser);
        }else {
            throw new AppException("Rol o Status invalido", HttpStatus.BAD_REQUEST);
        }
    }
}