package com.excelsisproject.productservice.services;

import jakarta.mail.util.ByteArrayDataSource;

public interface EmailService {

    void changePasswordEmail(String to, String token);

    void sendInvoiceEmail(String to, ByteArrayDataSource dataSource);

    void sendTicketEmail(String to, ByteArrayDataSource dataSource);

    void registrationConfirmationEmail(String to, String token);

    void changeEmailRequest(String to, String token);
}
