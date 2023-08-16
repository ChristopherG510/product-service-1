package com.excelsisproject.productservice.controllers;


import com.excelsisproject.productservice.entities.User;
import com.excelsisproject.productservice.jwt.JwtGenerator;
import com.excelsisproject.productservice.dto.CredentialsDto;
import com.excelsisproject.productservice.dto.SignUpDto;
import com.excelsisproject.productservice.dto.UserDto;
import com.excelsisproject.productservice.repositories.UserRepository;
import com.excelsisproject.productservice.services.ConfirmationTokenService;
import com.excelsisproject.productservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public UserController(UserService userService, UserRepository userRepository,
                          AuthenticationManager authenticationManager, JwtGenerator jwtGenerator, ConfirmationTokenService confirmationTokenService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
        this.confirmationTokenService = confirmationTokenService;
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
        return userService.confirmToken(token);
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

    @GetMapping("/editMyUser")
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
    public String  requestPasswordChange(@RequestPart("login") CredentialsDto credentialsDto,@RequestPart("password") SignUpDto signUpDto){
        userService.requestPasswordChange(credentialsDto, signUpDto);

        return "Confirmation request sent";
    }

    @GetMapping("/changePassword")
    public String changePassword(@RequestParam("token") String token){

        return userService.changePassword(token);
    }
}
