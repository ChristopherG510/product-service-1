package com.excelsisproject.productservice.mappers;

import com.excelsisproject.productservice.dto.CartItemDto;
import com.excelsisproject.productservice.entities.CartItem;

public class CartItemMapper {

    public static CartItemDto mapToCartItemDto(CartItem cartItem){
        return new CartItemDto(
                cartItem.getItemId(),
                cartItem.getUserId(),
                cartItem.getProductId(),
                cartItem.getProductName(),
                cartItem.getStatus(),
                cartItem.getAmount(),
                cartItem.getPrice()
        );
    }

    public static CartItem mapToCartItem(CartItemDto cartItemDto){
        return new CartItem(
                cartItemDto.getItemId(),
                cartItemDto.getUserId(),
                cartItemDto.getProductId(),
                cartItemDto.getProductName(),
                cartItemDto.getStatus(),
                cartItemDto.getAmount(),
                cartItemDto.getPrice()
        );
    }

}
