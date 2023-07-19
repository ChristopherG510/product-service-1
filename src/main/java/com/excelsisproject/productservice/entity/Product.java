package com.excelsisproject.productservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter // Para los getters y setters
@NoArgsConstructor // Constructor vacio
@AllArgsConstructor // Contructor con todos los atributos
@Entity
@Table(name = "Products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    // Se le puede agregar @Column(name ="***") para que en la db se vea diferente
    private Long id;
    private String name;
    private String description;
    private double amountInStock;
    private BigDecimal price;

}
