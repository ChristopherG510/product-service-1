package com.excelsisproject.productservice.services;

import com.excelsisproject.productservice.config.SecurityConfig;
import com.excelsisproject.productservice.dto.ConfirmationTokenDto;
import com.excelsisproject.productservice.dto.CredentialsDto;
import com.excelsisproject.productservice.dto.SignUpDto;
import com.excelsisproject.productservice.dto.UserDto;
import com.excelsisproject.productservice.entities.ResetPasswordData;
import com.excelsisproject.productservice.entities.Roles;
import com.excelsisproject.productservice.entities.User;
import com.excelsisproject.productservice.exceptions.AppException;
import com.excelsisproject.productservice.exceptions.ResourceNotFoundException;
import com.excelsisproject.productservice.mappers.ConfirmationTokenMapper;
import com.excelsisproject.productservice.mappers.UserMapper;
import com.excelsisproject.productservice.repositories.ConfirmationTokenRepository;
import com.excelsisproject.productservice.repositories.UserRepository;
import com.excelsisproject.productservice.entities.ConfirmationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    private static final String PSW_RESET_SENT = "PASSWORD RESET SENT";

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

        ConfirmationTokenDto confirmationTokenDto = confirmationTokenService.createToken(savedUser);
        confirmationTokenRepository.save(ConfirmationTokenMapper.mapToConfirmationToken(confirmationTokenDto));

        //emailService.registrationConfirmationEmail(user.getFirstName(), user.getUserEmail(), confirmationTokenDto.getConfirmationToken());
        emailService.registrationConfirmationEmail(user.getUserEmail(), confirmationTokenDto.getConfirmationToken());

        return UserMapper.toUserDto(savedUser);
    }

    public Long getLoggedUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUser = authentication.getName();

        User user = userRepository.findByLogin(loggedUser)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario" + loggedUser + "no existe"));

        return user.getId();
    }

    public List<UserDto> getAllUsers(){
        List<User> users = userRepository.findAll();

        return users.stream().map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public UserDto getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUser = authentication.getName();

        User user = userRepository.findByLogin(loggedUser)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario" + loggedUser + "no existe"));
        UserDto loggedUserDto = UserMapper.toUserDto(user);
        return loggedUserDto;
    }

    public UserDto editMyUser(UserDto updatedUser){
        User user = userRepository.findById(getLoggedUserId()).stream().findFirst().orElseThrow(
                () -> new ResourceNotFoundException("Usuario no existe con id: " + getLoggedUserId()));

        Optional<User> optionalUser = userRepository.findByLoginOrUserEmail(updatedUser.getLogin(), updatedUser.getUserEmail());

        if (optionalUser.isPresent()){
            if(!(Objects.equals(updatedUser.getLogin(), optionalUser.get().getLogin()) || Objects.equals(updatedUser.getUserEmail(), optionalUser.get().getUserEmail()))) {
                throw new AppException("El Usuario o Email ya esta registrado", HttpStatus.BAD_REQUEST);
            }
        }

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setUserPhoneNumber(updatedUser.getUserPhoneNumber());
        user.setLogin(updatedUser.getLogin());
        user.setRuc(updatedUser.getRuc());
        User updatedUserObj = userRepository.save(user);

        return UserMapper.toUserDto(updatedUserObj);
    }

    public void requestChangeEmail(String newEmail){
        User user = userRepository.findById(getLoggedUserId()).orElseThrow(
                () -> new ResourceNotFoundException("Usuario no existe con id: " + getLoggedUserId()));

        Optional<User> optionalUser = userRepository.findByLoginOrUserEmail(newEmail, newEmail);
        if (optionalUser.isPresent()){
            throw new AppException("El Email ya se encuentra registrado", HttpStatus.BAD_REQUEST);
        }

        ConfirmationTokenDto token = confirmationTokenService.createEmailToken(user, newEmail);
        confirmationTokenRepository.save(ConfirmationTokenMapper.mapToConfirmationToken(token));
        emailService.changeEmailRequest(newEmail, token.getConfirmationToken());
    }

    public String changeEmail(String token){
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token);
        User user = confirmationToken.getUser();
        user.setUserEmail(confirmationToken.getTemp());
        confirmationTokenService.setConfirmedAt(token);

        userRepository.save(user);

        return "Correo cambiado exitosamente";
    }

    public String changePassword(ResetPasswordData resetPasswordData){

        User user = userRepository.findByLogin(getUser().getLogin()).orElseThrow(
                () -> new AppException("El Usuario o Email no existe.", HttpStatus.NOT_FOUND));

        if(securityConfig.passwordEncoder().matches(CharBuffer.wrap(resetPasswordData.getOldPassword()), user.getPassword())){
            if (Objects.equals(resetPasswordData.getPassword(), resetPasswordData.getRepeatPassword())){
                user.setPassword(securityConfig.passwordEncoder().encode(CharBuffer.wrap(resetPasswordData.getPassword())));
                userRepository.save(user);
                throw new AppException("Contraseña cambiada correctamente.", HttpStatus.OK);
            } else {
                throw new AppException("Las contraseñas no coinciden", HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new AppException("Contraseña actual incorrecta", HttpStatus.BAD_REQUEST);
        }
    }

    public void  forgotPassword(String login){
        User user = userRepository.findByLoginOrUserEmail(login,login).stream().findFirst().orElseThrow(
                () -> new AppException("El Usuario o Email no existe.", HttpStatus.NOT_FOUND));
        ConfirmationTokenDto token = confirmationTokenService.createPasswordToken(user);
        confirmationTokenRepository.save(ConfirmationTokenMapper.mapToConfirmationToken(token));
        emailService.changePasswordEmail(user.getUserEmail(), token.getConfirmationToken());
    }

    public String resetPassword(ResetPasswordData resetPasswordData){
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByConfirmationToken(resetPasswordData.getToken())
                .orElseThrow(() -> new AppException("Token no valido", HttpStatus.BAD_REQUEST));
        User user = confirmationToken.getUser();
        if (Objects.equals(resetPasswordData.getPassword(), resetPasswordData.getRepeatPassword())){
            user.setPassword(securityConfig.passwordEncoder().encode(CharBuffer.wrap(resetPasswordData.getPassword())));
            userRepository.save(user);
            confirmationToken.setTimeConfirmed(LocalDateTime.now());
            confirmationToken.setStatus(TOKEN_VERIFIED);
            confirmationTokenRepository.save(confirmationToken);
            return "Contraseña cambiada correctamente.";
        } else {
            throw new AppException("Las contraseñas no coinciden", HttpStatus.BAD_REQUEST);
        }
    }

    public UserDto editUserRoleOrStatus(UserDto updatedUser){
        User user = userRepository.findById(updatedUser.getId()).stream().findFirst().orElseThrow(
                () -> new ResourceNotFoundException("Usuario no existe con id: " + updatedUser.getId()));
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