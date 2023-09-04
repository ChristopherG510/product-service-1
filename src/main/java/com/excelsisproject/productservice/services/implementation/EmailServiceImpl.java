package com.excelsisproject.productservice.services.implementation;

import com.excelsisproject.productservice.services.ConfirmationTokenService;
import com.excelsisproject.productservice.services.EmailService;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.verify.host}")
    private String host;
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${mail.urlFrontPassword}")
    private String urlFrontPassword;

    @Value("${mail.urlFrontVerify}")
    private String urlFrontVerify;

    @Value("${mail.urlFrontProfile}")
    private String urlProfile;


    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;

    private ConfirmationTokenService tokenService;
    private static final String VERIFICATION_URL = "http://localhost:8080/confirmar?token=";

    @Override
    public void changePasswordEmail(String to, String token){

        try {
            Context context = new Context();
            context.setVariable("url", urlFrontPassword + token);
            String text = templateEngine.process("change_password_email", context);

            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setPriority(1);
            helper.setSubject("Solicitud cambio de Contraseña");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(text, true);
            emailSender.send(message);
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void sendInvoiceEmail(String to, ByteArrayDataSource dataSource) {

        try {
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setPriority(1);
            helper.setSubject("Factura de Compra");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText("");
            helper.addAttachment("factura.pdf", dataSource);
            emailSender.send(message);
        } catch (Exception exception){
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public void sendTicketEmail(String to, ByteArrayDataSource dataSource) {
        try {
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setPriority(1);
            helper.setSubject("Ticket de Compra");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText("");
            helper.addAttachment("ticket.pdf", dataSource);
            emailSender.send(message);
        } catch (Exception exception){
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public void registrationConfirmationEmail(String to, String token) {
        try {
            Context context = new Context();
            context.setVariable("url", urlFrontVerify + token);
            String text = templateEngine.process("account_verification_email", context);

            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setPriority(1);
            helper.setSubject("Nueva Autenticación de Usuario");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(text, true);
            emailSender.send(message);
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void changeEmailRequest(String to, String token) {
        try {
            Context context = new Context();
            context.setVariable("url", urlProfile + token);
            String text = templateEngine.process("email_change_request", context);

            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setPriority(1);
            helper.setSubject("Solicitud de cambio de correo electronico");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setText(text, true);
            emailSender.send(message);
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private MimeMessage getMimeMessage() {
        return emailSender.createMimeMessage();
    }
}
