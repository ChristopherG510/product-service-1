package com.excelsisproject.productservice.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long orderId;
    private String orderUserName;
    private String orderUserAddress;
    private String orderContact;
    private Long productId;
    private Double orderAmount;
}
