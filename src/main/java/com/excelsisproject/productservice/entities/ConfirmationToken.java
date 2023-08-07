package com.excelsisproject.productservice.entities;

import com.excelsisproject.productservice.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String confirmationToken;
    private LocalDateTime timeCreated;
    private LocalDateTime timeExpired;
    private LocalDateTime timeConfirmed;
    @ManyToOne
    @JoinColumn(
            name = "user_id"
    )
    private User user;

}
