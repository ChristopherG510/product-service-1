package com.excelsisproject.productservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

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
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    private double amountInStock;
    private double price;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "product_images",
            joinColumns = {
                    @JoinColumn(name = "product_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "image_id")
            }
    )
    private Set<ImageModel> imageFiles;
    // @Column(name = "image", columnDefinition = "LONGBLOB")

}
