package com.excelsisproject.productservice.services.implementation;

import com.excelsisproject.productservice.dto.ConfirmationTokenDto;
import com.excelsisproject.productservice.entities.ConfirmationToken;
import com.excelsisproject.productservice.entities.User;
import com.excelsisproject.productservice.services.ConfirmationTokenService;
import com.excelsisproject.productservice.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.verify.host}")
    private String host;
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${mail.urlFront}")
    private String urlFront;

    private final JavaMailSender emailSender;

    private ConfirmationTokenService tokenService;


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
    public void changePasswordEmail(String to, String token){

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("Solicitud de cambio de contraseña");
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText(urlFront + token);
            emailSender.send(message);
        } catch (Exception exception){
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }
}
