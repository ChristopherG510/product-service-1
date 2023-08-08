package com.excelsisproject.productservice.dto;

import com.excelsisproject.productservice.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmationTokenDto {

    private Long id;
    private String confirmationToken;
    private LocalDateTime timeCreated;
    private LocalDateTime timeExpired;
    private LocalDateTime timeConfirmed;
    private String status;
    private User user;
}
