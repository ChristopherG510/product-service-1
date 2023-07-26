package com.excelsisproject.productservice.Jwt;

import com.excelsisproject.productservice.entities.User;
import com.excelsisproject.productservice.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j
public class JwtUtils {


    @Value("${jwt.secret.key}") // SecretKey para firmar el token
    private String secretKey;

    @Value("${jwt.time.expiration}") // Tiempo de expiracion del token
    private String timeExpiration;


    // Generar token de acceso
    public String generateAccesToken(String login){ // Se le pasa el username de la persona que va a generar el token
        return Jwts.builder()
                .setSubject(login) // Usuario que genera el token
                .setIssuedAt(new Date(System.currentTimeMillis())) // Fecha de creacion del token
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration))) // Fecha de expiracion
                .signWith(getSignatureKey(), SignatureAlgorithm.HS256) // Se firma el token
                .compact();
    }

    // Validar el token de acceso
    public boolean isTokenValid(String token){
        try{
            Jwts.parserBuilder() // Decodifica el token para obtener su fecha de creacion, fecha de expiracion,
                                // el usuario que genero el token, y la firma.
                    .setSigningKey(getSignatureKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        }catch (Exception e){
            log.error("Token invalido, error: ".concat(e.getMessage()));
            return false;
        }
    }

    // Obtener el username del token
    public String getUsernameFromToken(String token){
        return getClaim(token, Claims::getSubject);
    }

    // Obtener un solo claim
    public <T> T getClaim(String token, Function<Claims, T> claimsTFunction){
        Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    // Obtener todos los claims del token
    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignatureKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Obtener firma del token
    public Key getSignatureKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}