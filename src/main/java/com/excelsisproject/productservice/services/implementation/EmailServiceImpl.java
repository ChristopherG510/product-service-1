package com.excelsisproject.productservice.services.implementation;

import com.excelsisproject.productservice.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.verify.host}")
    private String host;
    @Value("${spring.mail.username}")
    private String fromEmail;

    private final JavaMailSender emailSender;


    @Override
    public void registrationConfirmationEmail(String name, String to, String token) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("Nueva autenticacion de usuario");
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText("Link de verificación: http://localhost:8080/confirmar?token=" + token);
            emailSender.send(message);

        } catch (Exception exception){
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public void passwordChangeEmail(String name, String to, String token){

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("Solicitud de cambio de contraseña");
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText("Link de Verificación de contraseña: http://localhost:8080/nuevaContrasena?token=" + token);
        } catch (Exception exception){
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }
}
