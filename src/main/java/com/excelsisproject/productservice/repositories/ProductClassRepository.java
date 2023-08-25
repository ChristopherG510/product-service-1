package com.excelsisproject.productservice.repositories;

import com.excelsisproject.productservice.entities.ProductClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductClassRepository extends JpaRepository<ProductClass, Long> {
}
