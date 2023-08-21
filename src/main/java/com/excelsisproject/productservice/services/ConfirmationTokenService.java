package com.excelsisproject.productservice.services;

import com.excelsisproject.productservice.dto.ConfirmationTokenDto;
import com.excelsisproject.productservice.entities.ConfirmationToken;
import com.excelsisproject.productservice.entities.User;

public interface ConfirmationTokenService {

    ConfirmationTokenDto createToken(User user);

    String createNewToken(String oldToken);

    ConfirmationTokenDto createPasswordToken(User user);

    String createNewPasswordToken(String oldToken);

    String verifyToken(String token);

    void saveConfirmationToken(ConfirmationToken token);

    ConfirmationToken getToken(String token);

    void setConfirmedAt(String token);

}
