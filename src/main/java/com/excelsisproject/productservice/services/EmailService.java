package com.excelsisproject.productservice.services;

public interface EmailService {
    void sendSimpleMailMessage(String name, String to, String token);
}
