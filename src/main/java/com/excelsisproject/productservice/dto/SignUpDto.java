package com.excelsisproject.productservice.dto;

import com.excelsisproject.productservice.entities.UserInfo;

import java.util.Set;

// Credenciales para el registro de usuarios
public record SignUpDto (String firstName, String lastName, UserInfo userInfo, String login, char[] password){
}
