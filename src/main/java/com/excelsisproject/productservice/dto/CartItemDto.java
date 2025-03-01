package com.excelsisproject.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private Long itemId;
    private Long userId;
    private Long productId;
    private String productName;
    private String status;
    private double amount;
    private double price;
}
