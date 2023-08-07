package com.excelsisproject.productservice.repositories;

import com.excelsisproject.productservice.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);



//    Page<Product> getProductsByIdIsTrueOrderByAmountInStock(Pageable pageable);
//
//    Page<Product> getProductsByIdIsTrueOrderByPriceAsc(Pageable pageable);
//
//    Page<Product> getProductsByIdIsTrueOrderByPriceDesc(Pageable pageable);
//
//    Page<Product> getProductsByIdIsTrueOrderByNameAsc(Pageable pageable);
//
//    Page<Product> getProductsByIdIsTrueOrderByNameDesc(Pageable pageable);


}
