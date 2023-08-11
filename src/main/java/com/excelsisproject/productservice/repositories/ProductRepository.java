package com.excelsisproject.productservice.repositories;

import com.excelsisproject.productservice.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    List<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);

    Page<Product> findAllByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Product> findAllByPrice(double price, Pageable pageable);

    Page<Product> findAllByDescriptionContainingIgnoreCase(String name, Pageable pageable);

}
