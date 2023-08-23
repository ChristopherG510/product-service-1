package com.excelsisproject.productservice.services;

import com.excelsisproject.productservice.entities.User;
import jakarta.mail.util.ByteArrayDataSource;

public interface EmailService {
    void registrationConfirmationEmail(String name, String to, String token);

    void changePasswordEmail(String to, String token);

    void sendInvoiceEmail(String to, ByteArrayDataSource dataSource);
}
