package com.excelsisproject.productservice.repositories;

import com.excelsisproject.productservice.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    List<Order> findByDateOrdered(String dateOrdered);

    List<Order> findByUserId(Long userId);

    Page<Order> findAllByOrderStatus(String status, Pageable pageable);




}
