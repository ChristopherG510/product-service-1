package com.excelsisproject.productservice.filters;


import com.excelsisproject.productservice.Jwt.JwtUtils;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtils jwtUtils;

    public JwtAuthenticationFilter(JwtUtils jwtUtils){
        this.jwtUtils = jwtUtils;
    }

    @Override
    // Metodo para intentar autenticar un usuario
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        com.excelsisproject.productservice.entities.User userEntity = null;
        String login = "";
        String password = "";
        try{
            // Como el request viene en formato JSON, se debe a mapear a un objeto de tipo User:
            userEntity = new ObjectMapper().readValue(request.getInputStream(), com.excelsisproject.productservice.entities.User.class);
            // Una vez mapeado el JSON, se obtienen el username y el password del usuario que intenta autenticarse
            login = userEntity.getLogin();
            password = userEntity.getPassword();
        } catch (StreamReadException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(login, password);

        return getAuthenticationManager().authenticate(authenticationToken);
    }


    @Override
    // Si la autenticacion fue correcta, se genera el token de acceso
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        // Se obtiene el objeto que contiene todos los detalles de usuario, se usa la clase User propia de Spring
        User user = (User) authResult.getPrincipal();

        // Se genera el token de acceso
        String token = jwtUtils.generateAccesToken(user.getUsername());

        // Se responde en el header de la respuesta con el token de acceso
        response.addHeader("Authorization", token);

        // Se responde en el cuerpo de la respuesta con los siguientes datos:
        // Se mapea la respuesta a un JSON
        Map<String, Object> httpResponse = new HashMap<>();
        httpResponse.put("Token", token);
        httpResponse.put("Message", "Autenticacion Correcta");
        httpResponse.put("Username", user.getUsername());
        httpResponse.put("Rol",user.getAuthorities());
        response.getWriter().write(new ObjectMapper().writeValueAsString(httpResponse));

        // Se retorna un codigo 200
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().flush();

        super.successfulAuthentication(request, response, chain, authResult);
    }
}