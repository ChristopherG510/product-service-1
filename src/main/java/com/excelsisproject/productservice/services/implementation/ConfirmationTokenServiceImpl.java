package com.excelsisproject.productservice.services.implementation;

import com.excelsisproject.productservice.entities.ConfirmationToken;
import com.excelsisproject.productservice.repositories.ConfirmationTokenRepository;
import com.excelsisproject.productservice.services.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService{

    private ConfirmationTokenRepository confirmationTokenRepository;

    @Override
    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    @Override
    public ConfirmationToken getToken(String token) {
        return confirmationTokenRepository.findByConfirmationToken(token);
    }

    @Override
    public void setConfirmedAt(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByConfirmationToken(token);
        confirmationToken.setTimeConfirmed(LocalDateTime.now());
    }


}
