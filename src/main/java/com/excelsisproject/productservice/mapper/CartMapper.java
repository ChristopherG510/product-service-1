package com.excelsisproject.productservice.mapper;

import com.excelsisproject.productservice.dto.CartDto;
import com.excelsisproject.productservice.entity.Cart;

public class CartMapper {

    public static CartDto mapToCartDto(Cart cart){
        return new CartDto(
                cart.getId(),
                cart.getProductId(),
                cart.getAmount(),
                cart.getPrice()
        );
    }

    public static Cart mapToCart(CartDto cartDto){
        return new Cart(
                cartDto.getId(),
                cartDto.getProductId(),
                cartDto.getAmount(),
                cartDto.getPrice()
        );
    }
}
