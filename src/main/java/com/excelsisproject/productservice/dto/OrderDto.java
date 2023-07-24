package com.excelsisproject.productservice.dto;

import com.excelsisproject.productservice.entity.Cart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long orderId;
    private String orderUserName;
    private String orderUserAddress;
    private String orderContact;
    private String dateOrdered;
    private String timeOrdered;
    private List<Cart> cartItems;
    private double totalPrice;

}


