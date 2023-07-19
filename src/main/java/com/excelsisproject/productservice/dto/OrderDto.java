package com.excelsisproject.productservice.dto;

import com.excelsisproject.productservice.entity.Cart;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
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
    private List<Cart> cartItems;
    private double totalPrice;
//
//    public List<Cart> getCartItems(){
//        return cartItems;
//    }
//
//    public void setCartItems(List<Cart> newCartItems){
//        this.cartItems = newCartItems;
//    }
//
}


