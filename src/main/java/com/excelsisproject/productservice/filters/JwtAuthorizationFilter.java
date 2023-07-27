package com.excelsisproject.productservice.filters;

import com.excelsisproject.productservice.Jwt.JwtUtils;
import com.excelsisproject.productservice.services.implementation.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Se recupera el token del header Authorization
        String tokenHeader = request.getHeader("Authorization");

        if(tokenHeader != null && tokenHeader.startsWith("Bearer ")){ // Se verifica que el token no es nulo y que empieza con la palabra Bearer

            // Se separa la palabra Bearer del token
            String token = tokenHeader.substring(7);

            if(jwtUtils.isTokenValid(token)){ // Se verifica que el token es valido
                String login = jwtUtils.getUsernameFromToken(token); // Si el token es valido, se recupera el userName

                UserDetails userDetails = userDetailsService.loadUserByUsername(login); // Se recuperan los detalles del usuario

                // Se autentica el usuario con su userName, contraseña y sus roles
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(login, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}