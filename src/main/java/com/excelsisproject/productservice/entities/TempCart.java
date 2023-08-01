package com.excelsisproject.productservice.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "temp_cart")
public class TempCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
//    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = Cart.class)
//    private Cart product;
}
