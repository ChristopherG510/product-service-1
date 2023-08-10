package com.excelsisproject.productservice.repositories;

import com.excelsisproject.productservice.entities.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacturaRepository extends JpaRepository<Factura, Long> {

    List<Factura> findAllByUserId(Long userId);
}
