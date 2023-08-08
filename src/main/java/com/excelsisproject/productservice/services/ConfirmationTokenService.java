package com.excelsisproject.productservice.services;

import com.excelsisproject.productservice.entities.ConfirmationToken;

public interface ConfirmationTokenService {

    void saveConfirmationToken(ConfirmationToken token);

    ConfirmationToken getToken(String token);

    void setConfirmedAt(String token);

}
