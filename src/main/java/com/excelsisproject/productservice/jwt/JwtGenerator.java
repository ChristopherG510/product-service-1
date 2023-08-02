package com.excelsisproject.productservice.jwt;

import com.excelsisproject.productservice.jwt.ConstantesSeguridad;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtGenerator {

    // Metodo para generar un token jwt
    public String generarToken(Authentication authentication){
        String login = authentication.getName();
        Date tiempo_actual = new Date();
        Date expiracionToken = new Date(tiempo_actual.getTime() + ConstantesSeguridad.JWT_EXPIRATION_TOKEN);

        return Jwts.builder()
                .setSubject(login) // Usuario que esta iniciando la sesion
                .setIssuedAt(new Date()) // Fecha de emision del token
                .setExpiration(expiracionToken) // Fecha de expiracion del token
                .signWith(SignatureAlgorithm.HS512, ConstantesSeguridad.JWT_FIRMA) // Se firma el token con la secret key
                .compact();
    }

    //Método para extraer un Username apartir de un token
    public String obtenerUserNameToken(String token){
        Claims claims = Jwts.parser()// El método parser se utiliza con el fin de analizar el token
                .setSigningKey(ConstantesSeguridad.JWT_FIRMA)// Establece la clave de firma, que se utiliza para verificar la firma del token
                .parseClaimsJws(token)//Se utiliza para verificar la firma del token, apartir del String "token"
                .getBody();/*Se obtiene el cuerpo ya verificado del token el cual contendrá la información de
                nombre de usuario, fecha de expiración y firma del token*/

        return claims.getSubject();//Devolvemos el nombre de usuario
    }


    //Metodo para validar el token
    public Boolean validarToken( String token){
        try{
            //Validación del token por medio de la firma que contiene el String token(token)
            //Si son idénticas validara el token o caso contrario saltara la excepción de abajo
            Jwts.parser().setSigningKey(ConstantesSeguridad.JWT_FIRMA).parseClaimsJws(token);
            return true;
        }catch (Exception e){
            throw new AuthenticationCredentialsNotFoundException("Su token ha expirado");

        }
    }


<<<<<<< HEAD
}
=======
}
>>>>>>> 7561cfaf590de84a61d7d2a265997032f35385e5
