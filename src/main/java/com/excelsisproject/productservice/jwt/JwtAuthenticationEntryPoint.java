package com.excelsisproject.productservice.jwt;

import com.excelsisproject.productservice.dto.ErrorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
//Clase para poder manejar las excepciones de tipo autenticación
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        // Se obtiene el tipo de AuthenticationException
        Class<? extends AuthenticationException> exceptionClass = authException.getClass();

        // Se lanza un mensaje personalizado de acuerdo al tipo de excepcion que se presente

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        if (exceptionClass == BadCredentialsException.class) {
            OBJECT_MAPPER.writeValue(response.getOutputStream(), new ErrorDto("Credenciales inválidas"));
        } else {
            OBJECT_MAPPER.writeValue(response.getOutputStream(), new ErrorDto("Acceso no va"));
        }
    }
}