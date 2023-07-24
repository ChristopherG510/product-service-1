package com.excelsisproject.productservice.repository;

import com.excelsisproject.productservice.dto.OrderDto;
import com.excelsisproject.productservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByDateOrdered(String dateOrdered);

}
