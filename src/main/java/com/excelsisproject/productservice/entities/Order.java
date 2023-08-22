package com.excelsisproject.productservice.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    @Column(name = "user_id")
    private Long userId;
    private String firstName;
    private String lastName;
    private String userEmail;
    private String userPhoneNumber;
    @Column(nullable = false)
    private String userAddress;
    private String ruc;
    @Column(nullable = false)
    private String paymentMethod;
    private String orderDescription;
    private String orderStatus;
    @Column(name = "order_date")
    private LocalDate dateOrdered;
    @Column(name = "order_time")
    private LocalTime timeOrdered;
    @Column(nullable = false)
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = CartItem.class)
    @JoinColumn(name = "order_id", referencedColumnName = "orderId")
    private List<CartItem> cartItems;
    private double totalPrice;
}
