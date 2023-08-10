package com.excelsisproject.productservice.services.implementation;

import com.excelsisproject.productservice.entities.ConfirmationToken;
import com.excelsisproject.productservice.entities.User;
import com.excelsisproject.productservice.repositories.ConfirmationTokenRepository;
import com.excelsisproject.productservice.services.ConfirmationTokenService;
import com.excelsisproject.productservice.services.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService{

    private ConfirmationTokenRepository confirmationTokenRepository;
    private EmailService emailService;

    @Override
    public String createNewToken(String oldToken) { // crea nuevo token si el del usuario ya ha expirado
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByConfirmationToken(oldToken);
        User user = confirmationToken.getUser();

        confirmationToken.setConfirmationToken(UUID.randomUUID().toString());
        confirmationToken.setStatus("SENT");
        confirmationToken.setTimeCreated(LocalDateTime.now());
        confirmationToken.setTimeExpired(LocalDateTime.now().plusMinutes(15));
        confirmationToken.setTimeConfirmed(null);
        saveConfirmationToken(confirmationToken);

        emailService.sendSimpleMailMessage(user.getFirstName(), user.getUserEmail(), confirmationToken.getConfirmationToken());

        return "Nuevo token generado";
    }

    @Override
    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    @Override
    public ConfirmationToken getToken(String token) {
        return confirmationTokenRepository.findByConfirmationToken(token);
    }

    @Override
    public void setConfirmedAt(String token) {  // agrega la fecha de confirmacion del token
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByConfirmationToken(token);
        confirmationToken.setTimeConfirmed(LocalDateTime.now());
    }


}
