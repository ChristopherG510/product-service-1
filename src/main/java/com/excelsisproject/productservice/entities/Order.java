package com.excelsisproject.productservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @OneToOne(cascade = CascadeType.ALL)
    private UserInfo userInfo;
    @Column(name = "order_date")
    private String dateOrdered;
    @Column(name = "order_time")
    private String timeOrdered;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = Cart.class)
    private List<Cart> cartItems;
    private double totalPrice;

}
