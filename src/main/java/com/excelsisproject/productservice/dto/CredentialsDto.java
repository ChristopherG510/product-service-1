package com.excelsisproject.productservice.dto;

import lombok.Data;

// Credenciales para el login de usuarios
@Data
public class CredentialsDto{
    private String login;
    private String password;
}
