package com.excelsisproject.productservice.repositories;

import com.excelsisproject.productservice.entities.ProductClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductClassRepository extends JpaRepository<ProductClass, Long>, JpaSpecificationExecutor<ProductClass> {

    Optional<ProductClass> getByNameIgnoreCase(String name);
}
