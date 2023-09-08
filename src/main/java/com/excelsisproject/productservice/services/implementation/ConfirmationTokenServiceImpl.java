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
    private static final String EMAIL_CHANGE_SENT = "EMAIL CHANGE SENT";
    private static final String EMAIL_CHANGE_EXPIRED = "EMAIL CHANGE EXPIRED";
    private static final String TOKEN_INVALID = "INVALID";

    @Override
    public ConfirmationTokenDto createToken(User user) {

        ConfirmationTokenDto confirmationTokenDto = new ConfirmationTokenDto();
        confirmationTokenDto.setConfirmationToken(UUID.randomUUID().toString());
        confirmationTokenDto.setTimeCreated(LocalDateTime.now());
        confirmationTokenDto.setTimeExpired(LocalDateTime.now().plusMinutes(1));
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
        confirmationToken.setTimeExpired(LocalDateTime.now().plusMinutes(1));
        confirmationToken.setTimeConfirmed(null);
        saveConfirmationToken(confirmationToken);

        //emailService.registrationConfirmationEmail(user.getFirstName(), user.getUserEmail(), confirmationToken.getConfirmationToken());
        emailService.registrationConfirmationEmail(user.getUserEmail(), confirmationToken.getConfirmationToken());

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

        throw new AppException("Nuevo token generado", HttpStatus.OK);
    }

    public String verifyToken(String token){
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByConfirmationToken(token)
                .orElseThrow(() -> new AppException("Token no valido", HttpStatus.BAD_REQUEST));
        User user = confirmationToken.getUser();

        if(LocalDateTime.now().isAfter(confirmationToken.getTimeExpired()) && (!Objects.equals(confirmationToken.getStatus(), TOKEN_VERIFIED))) {
            confirmationToken.setStatus(TOKEN_EXPIRED);
            confirmationTokenRepository.save(confirmationToken);
            throw new AppException("Su token ha expirado. Haga clic aqui para crear uno nuevo. http://localhost:8080/newToken?token=" + token, HttpStatus.BAD_REQUEST);

        } else if (LocalDateTime.now().isAfter(confirmationToken.getTimeExpired()) && (Objects.equals(confirmationToken.getStatus(), PSW_RESET_SENT))){
            confirmationToken.setStatus(PSW_RESET_EXPIRED);
            confirmationTokenRepository.save(confirmationToken);
            throw new AppException("Su token ha expirado. Haga clic aqui para crear uno nuevo. http://localhost:8080/newPasswordToken?token=" + token, HttpStatus.BAD_REQUEST);

        } else if (Objects.equals(confirmationToken.getStatus(), TOKEN_VERIFIED)) {
            throw new AppException("Usuario ya Verificado.", HttpStatus.OK);

        } else if (Objects.equals(confirmationToken.getStatus(), TOKEN_SENT)) {
            confirmationToken.setStatus(TOKEN_VERIFIED);
            Set<Roles> roles = user.getRoles();
            // Cambiar el rol PENDIENTE a CLIENTE
            for (Roles role : roles) {
                role.setName("CLIENTE");
            }
            user.setRoles(roles);

            confirmationToken.setTimeConfirmed(LocalDateTime.now());
            confirmationTokenRepository.save(confirmationToken);
            userRepository.save(user);

            throw new AppException("Usuario Verificado.", HttpStatus.OK);

        } else if (Objects.equals(confirmationToken.getStatus(), PSW_RESET_SENT)){
            confirmationToken.setStatus(TOKEN_VERIFIED);
            confirmationToken.setTimeConfirmed(LocalDateTime.now());
            confirmationTokenRepository.save(confirmationToken);

            throw new AppException("La contraseña ya ha sido verificada.", HttpStatus.OK);
        } else {
            throw new AppException("Token inválido.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    @Override
    public ConfirmationTokenDto createEmailToken(User user, String newEmail) {
        ConfirmationTokenDto confirmationTokenDto = new ConfirmationTokenDto();
        confirmationTokenDto.setConfirmationToken(UUID.randomUUID().toString());
        confirmationTokenDto.setTimeCreated(LocalDateTime.now());
        confirmationTokenDto.setTimeExpired(LocalDateTime.now().plusMinutes(15));
        confirmationTokenDto.setTimeConfirmed(null);
        confirmationTokenDto.setUser(user);
        confirmationTokenDto.setStatus(EMAIL_CHANGE_SENT);
        confirmationTokenDto.setTemp(newEmail);
        return confirmationTokenDto;
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
        confirmationToken.setTemp(null);
        confirmationToken.setStatus(TOKEN_VERIFIED);
        confirmationTokenRepository.save(confirmationToken);
    }
}
