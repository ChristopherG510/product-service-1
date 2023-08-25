package com.excelsisproject.productservice.controllers;


import com.excelsisproject.productservice.entities.ResetPasswordData;
import com.excelsisproject.productservice.entities.User;
import com.excelsisproject.productservice.exceptions.AppException;
import com.excelsisproject.productservice.jwt.JwtGenerator;
import com.excelsisproject.productservice.dto.CredentialsDto;
import com.excelsisproject.productservice.dto.SignUpDto;
import com.excelsisproject.productservice.dto.UserDto;
import com.excelsisproject.productservice.repositories.ConfirmationTokenRepository;
import com.excelsisproject.productservice.repositories.UserRepository;
import com.excelsisproject.productservice.services.ConfirmationTokenService;
import com.excelsisproject.productservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

// Controlador para el registro y login de usuarios

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private JwtGenerator jwtGenerator;
    private final ConfirmationTokenService confirmationTokenService;
    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository,
                          AuthenticationManager authenticationManager, JwtGenerator jwtGenerator,
                          ConfirmationTokenService confirmationTokenService, ConfirmationTokenRepository confirmationTokenRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
        this.confirmationTokenService = confirmationTokenService;
        this.confirmationTokenRepository = confirmationTokenRepository;
    }


    // Login de Usuarios
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody CredentialsDto credentialsDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                credentialsDto.getLogin(), credentialsDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generarToken(authentication);
        UserDto userDto = userService.login(credentialsDto);
        userDto.setToken(token);
        return ResponseEntity.ok(userDto);
    }

    // Registros de Usuarios
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody SignUpDto singUpDto) {
        UserDto user = userService.register(singUpDto);
        return ResponseEntity.created(URI.create("/users/" + user.getId())).body(user);
    }

    @GetMapping("/confirmar")
    public String confirmToken(@RequestParam("token") String token) {
        return confirmationTokenService.verifyToken(token);
    }

    @GetMapping("/newToken")
    public String newToken(@RequestParam("token") String token) {
        return confirmationTokenService.createNewToken(token);
    }

    @DeleteMapping("/deleteUser")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteUser(@RequestParam Long id) {
        userRepository.deleteById(id);
        return "Se ha eliminado el user con id " + id;
    }

    @GetMapping("/getUsers")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/getMyUser")
    public ResponseEntity<UserDto> getMyUser() {
        return ResponseEntity.ok(userService.getUser());
    }

    @PutMapping("/editMyUser")
    public ResponseEntity<UserDto> editMyUser(@RequestBody UserDto userDto) {
        UserDto user = userService.editMyUser(userDto);
        return ResponseEntity.ok(user);
    }


    @PostMapping("/editUserStatus")
    public ResponseEntity<UserDto> editUserRoleOrStatus(@RequestBody UserDto userDto){
        UserDto user = userService.editUserRoleOrStatus(userDto);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/changePasswordRequest")
    public void  requestPasswordChange(@RequestBody CredentialsDto credentialsDto){
        userService.forgotPassword(credentialsDto.getLogin());
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody ResetPasswordData resetPasswordData){
        return userService.resetPassword(resetPasswordData);
    }

    @GetMapping("/newPasswordToken")
    public String newResetPasswordToken(@RequestParam("token") String token){
        User user = confirmationTokenRepository.findByConfirmationToken(token)
                .orElseThrow(() -> new AppException("Token not found", HttpStatus.NOT_FOUND)).getUser();
        confirmationTokenService.createPasswordToken(user);
        return "";
    }
}
