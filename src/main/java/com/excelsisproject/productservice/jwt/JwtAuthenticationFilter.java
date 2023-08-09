package com.excelsisproject.productservice.jwt;

import com.excelsisproject.productservice.services.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

/*La función de esta clase será validar la información del token y si esto es exitoso,
establecerá la autenticación de un usuario con sus roles en la solicitud o en el contexto de seguridad de nuestra aplicación*/

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtGenerator jwtGenerator;

    /*Este metodo extrea el token JWT de la cabecera de nuestra petición Http("Authorization"),
     luego lo valida y lo retorna*/
    private String obtenerTokenSolicitud(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,@NonNull FilterChain filterChain)
            throws ServletException, IOException {
        //Se obtienen los datos del token
        String token = obtenerTokenSolicitud(request);
        if (StringUtils.hasText(token) && jwtGenerator.validarToken(token)){
            //Se asigna el login contenido en el token a la variable login
            String login = jwtGenerator.obtenerUserNameToken(token);
            //Se crea el objeto userDetails el cual contendrá todos los detalles del user
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(login);
            //Se carga un set de strings con los roles del usuario
            Set<String> userRoles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
            //Se comprueba que el usuario autenticado posea alguno de los siguientes roles:
            if (userRoles.contains("CLIENTE") || userRoles.contains("ADMIN")){
                //Se crea el objeto UsernamePasswordAuthenticationToken el cual contendrá los detalles de autenticación del usuario
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,null,userDetails.getAuthorities());
                //Se establece información adicional de la autenticación, como por ejemplo la dirección ip del usuario,
                // o el agente de usuario para hacer la solicitud etc.
                authenticationToken.setDetails( new WebAuthenticationDetailsSource().buildDetails(request));
                //Se establece el objeto anterior (autenticación del usuario) en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        //Permite que la solicitud continue hacia el siguiente filtro en la cadena de filtro.
        filterChain.doFilter(request, response);
    }
}
