package com.excelsisproject.productservice.services.implementation;

import com.excelsisproject.productservice.dto.ConfirmationTokenDto;
import com.excelsisproject.productservice.entities.ConfirmationToken;
import com.excelsisproject.productservice.entities.Roles;
import com.excelsisproject.productservice.entities.User;
import com.excelsisproject.productservice.exceptions.AppException;
import com.excelsisproject.productservice.repositories.ConfirmationTokenRepository;
import com.excelsisproject.productservice.repositories.UserRepository;
import com.excelsisproject.productservice.services.ConfirmationTokenService;
import com.excelsisproject.productservice.services.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService{

    private ConfirmationTokenRepository confirmationTokenRepository;
    private UserRepository userRepository;
    private EmailService emailService;
    private static final String TOKEN_EXPIRED = "EXPIRED";
    private static final String TOKEN_VERIFIED = "VERIFIED";
    private static final String TOKEN_SENT = "SENT";

    private static final String PSW_RESET_SENT = "PASSWORD RESET SENT";
    private static final String PSW_RESET_EXPIRED = "PASSWORD RESET EXPIRED";
    private static final String TOKEN_INVALID = "INVALID";

    @Override
    public ConfirmationTokenDto createToken(User user) {

        ConfirmationTokenDto confirmationTokenDto = new ConfirmationTokenDto();
        confirmationTokenDto.setConfirmationToken(UUID.randomUUID().toString());
        confirmationTokenDto.setTimeCreated(LocalDateTime.now());
        confirmationTokenDto.setTimeExpired(LocalDateTime.now().plusMinutes(15));
        confirmationTokenDto.setTimeConfirmed(null);
        confirmationTokenDto.setUser(user);
        confirmationTokenDto.setStatus(TOKEN_SENT);

        return confirmationTokenDto;
    }

    @Override
    public String createNewToken(String oldToken) { // crea nuevo token si el del usuario ya ha expirado
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByConfirmationToken(oldToken)
                .orElseThrow(() -> new AppException("Token no valido", HttpStatus.BAD_REQUEST));
        User user = confirmationToken.getUser();

        confirmationToken.setConfirmationToken(UUID.randomUUID().toString());
        confirmationToken.setStatus(TOKEN_SENT);
        confirmationToken.setTimeCreated(LocalDateTime.now());
        confirmationToken.setTimeExpired(LocalDateTime.now().plusMinutes(15));
        confirmationToken.setTimeConfirmed(null);
        saveConfirmationToken(confirmationToken);

        emailService.registrationConfirmationEmail(user.getFirstName(), user.getUserEmail(), confirmationToken.getConfirmationToken());

        return "Nuevo token generado";
    }

    @Override
    public ConfirmationTokenDto createPasswordToken(User user) {

        ConfirmationTokenDto confirmationTokenDto = new ConfirmationTokenDto();
        confirmationTokenDto.setConfirmationToken(UUID.randomUUID().toString());
        confirmationTokenDto.setTimeCreated(LocalDateTime.now());
        confirmationTokenDto.setTimeExpired(LocalDateTime.now().plusMinutes(15));
        confirmationTokenDto.setTimeConfirmed(null);
        confirmationTokenDto.setUser(user);
        confirmationTokenDto.setStatus(PSW_RESET_SENT);
        return confirmationTokenDto;
    }

    @Override
    public String createNewPasswordToken(String oldToken) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByConfirmationToken(oldToken)
                .orElseThrow(() -> new AppException("Token no valido", HttpStatus.BAD_REQUEST));
        User user = confirmationToken.getUser();

        confirmationToken.setConfirmationToken(UUID.randomUUID().toString());
        confirmationToken.setStatus(PSW_RESET_SENT);
        confirmationToken.setTimeCreated(LocalDateTime.now());
        confirmationToken.setTimeExpired(LocalDateTime.now().plusMinutes(15));
        confirmationToken.setTimeConfirmed(null);
        saveConfirmationToken(confirmationToken);

        emailService.changePasswordEmail(user.getUserEmail(), confirmationToken.getConfirmationToken());

        return "Nuevo token generado";
    }

    public String verifyToken(String token){
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByConfirmationToken(token)
                .orElseThrow(() -> new AppException("Token no valido", HttpStatus.BAD_REQUEST));
        User user = confirmationToken.getUser();

        if(LocalDateTime.now().isAfter(confirmationToken.getTimeExpired()) && (!Objects.equals(confirmationToken.getStatus(), TOKEN_VERIFIED))) {
            confirmationToken.setStatus(TOKEN_EXPIRED);
            return "<HTML><body> <a href=\"http://localhost:8080/newToken?token=" + token + "\">Su token ha expirado. Haga clic aqui para crear uno nuevo.</a></body></HTML>";

        } else if (LocalDateTime.now().isAfter(confirmationToken.getTimeExpired()) && (Objects.equals(confirmationToken.getStatus(), PSW_RESET_SENT))){
            confirmationToken.setStatus(PSW_RESET_EXPIRED);
            return "<HTML><body> <a href=\"http://localhost:8080/newPasswordToken?token=" + token + "\">Su token ha expirado. Haga clic aqui para crear uno nuevo.</a></body></HTML>";

        } else if (Objects.equals(confirmationToken.getStatus(), TOKEN_VERIFIED)) {
            return TOKEN_VERIFIED;

        } else if (Objects.equals(confirmationToken.getStatus(), TOKEN_SENT)) {
            confirmationToken.setStatus(TOKEN_VERIFIED);
            Set<Roles> roles = user.getRoles();
            // Cambiar el rol PENDIENTE a CLIENTE
            for (Roles role : roles) {
                role.setName("ADMIN");
            }
            user.setRoles(roles);

            confirmationToken.setTimeConfirmed(LocalDateTime.now());
            confirmationTokenRepository.save(confirmationToken);
            userRepository.save(user);

            return "Usuario " + user.getLogin() + " Registrado";

        } else if (Objects.equals(confirmationToken.getStatus(), PSW_RESET_SENT)){
            confirmationToken.setStatus(TOKEN_VERIFIED);

            return TOKEN_VERIFIED;
        } else {
            return TOKEN_INVALID;
        }
    }

    @Override
    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    @Override
    public ConfirmationToken getToken(String token) {
        return confirmationTokenRepository.findByConfirmationToken(token)
                .orElseThrow(() -> new AppException("Token no valido", HttpStatus.BAD_REQUEST));
    }

    @Override
    public void setConfirmedAt(String token) {  // agrega la fecha de confirmacion del token
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByConfirmationToken(token)
                .orElseThrow(() -> new AppException("Token no valido", HttpStatus.BAD_REQUEST));
        confirmationToken.setTimeConfirmed(LocalDateTime.now());
    }


}
