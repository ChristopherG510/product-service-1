package com.excelsisproject.productservice.dto;

import java.util.Set;

// Credenciales para el registro de usuarios
public record SignUpDto (String firstName, String lastName, String login, char[] password){
}
