package com.excelsisproject.productservice.dto;

// Credenciales para el registro de usuarios
public record SignUpDto (String firstName, String lastName, String userEmail, String userPhoneNumber, String userAddress,String login, char[] password){
}
