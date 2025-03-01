package com.excelsisproject.productservice.repositories;

import com.excelsisproject.productservice.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findAllByUserId(Long userId);

    List<CartItem> findAllByUserIdAndStatus(Long userId, String status);
}
