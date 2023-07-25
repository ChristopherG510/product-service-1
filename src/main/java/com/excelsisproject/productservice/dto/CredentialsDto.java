package com.excelsisproject.productservice.dto;

// Credenciales para el login de usuarios
public record CredentialsDto(String login, char[] password) {
}
