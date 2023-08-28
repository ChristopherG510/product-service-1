package com.excelsisproject.productservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter // Para los getters y setters
@NoArgsConstructor // Constructor vacio
@AllArgsConstructor // Contructor con todos los atributos
@Entity
public class ProductClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productClassId;
    private String name;
    @Column(name = "description", columnDefinition = "TEXT", length = 2000)
    private String description;
    private String category;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", referencedColumnName = "productClassId")
    private List<Product> products;
    private double price;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "product_class_images",
            joinColumns = {
                    @JoinColumn(name = "product_class_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "image_id")
            }
    )
    private Set<ImageModel> imageFiles;
}
