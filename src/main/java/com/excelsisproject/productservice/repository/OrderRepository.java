package com.excelsisproject.productservice.repository;

import com.excelsisproject.productservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
