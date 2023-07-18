package com.excelsisproject.productservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter // Para los getters y setters
@NoArgsConstructor // Constructor vacio
@AllArgsConstructor // Contructor con todos los atributos
@Entity
@Table(name = "Users")
public class User { // Clase Usuario
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String lastName;
    private String userName;
    private String email;
    private String password;

}
