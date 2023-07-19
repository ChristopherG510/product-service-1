package com.excelsisproject.productservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
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
    @Column(name = "user")
    private String orderUserName;
    @Column(name = "address")
    private String orderUserAddress;
    @Column(name = "contact")
    private String orderContact;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = Cart.class)
    private List<Cart> cartItems;
    private double totalPrice;

}
