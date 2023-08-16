package com.excelsisproject.productservice.services;

public interface EmailService {
    void registrationConfirmationEmail(String name, String to, String token);

    void passwordChangeEmail(String name, String to, String newPassword);
}
