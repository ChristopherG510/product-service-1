package com.excelsisproject.productservice.repositories;

import com.excelsisproject.productservice.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByDateOrdered(String dateOrdered);

    List<Order> findByUserId(Long userId);

}
