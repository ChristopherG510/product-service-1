package com.excelsisproject.productservice.services;

import com.excelsisproject.productservice.dto.ConfirmationTokenDto;
import com.excelsisproject.productservice.entities.ConfirmationToken;
import com.excelsisproject.productservice.entities.User;

public interface ConfirmationTokenService {

    ConfirmationTokenDto createToken(User user);

    ConfirmationTokenDto createNewPasswordToken(User user, String newPassword);

    String createNewToken(String oldToken);

    void saveConfirmationToken(ConfirmationToken token);

    ConfirmationToken getToken(String token);

    void setConfirmedAt(String token);

}
