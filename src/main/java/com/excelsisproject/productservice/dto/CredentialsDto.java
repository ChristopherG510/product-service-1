package com.excelsisproject.productservice.dto;

// Credenciales para el login de usuarios

import lombok.Data;

@Data
public class CredentialsDto{
    private String login;
    private String password;
}
