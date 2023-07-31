 package com.excelsisproject.productservice.repositories;


import com.excelsisproject.productservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

 @Repository
 public interface UserRepository extends JpaRepository<User, Long>{
     //Método para poder buscar un usuario mediante su login
     Optional<User> findByLogin(String login);

     //Método para poder verificar si un usuario existe en la base de datos
     Boolean existsByLogin(String login);

 }