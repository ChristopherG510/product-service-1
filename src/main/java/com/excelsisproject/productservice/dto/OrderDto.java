package com.excelsisproject.productservice.dto;

import com.excelsisproject.productservice.entities.Cart;
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
    private Long userId;
    private String firstName;
    private String lastName;
    private String userEmail;
    private String userPhoneNumber;
    private String userAddress;
    private String dateOrdered;
    private String timeOrdered;
    private List<Cart> cartItems;
    private double totalPrice;


}


