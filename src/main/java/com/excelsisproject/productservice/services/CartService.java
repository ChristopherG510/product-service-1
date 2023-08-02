package com.excelsisproject.productservice.services;

import com.excelsisproject.productservice.dto.CartItemDto;

import java.util.List;

public interface CartService {

    CartItemDto addToCart(CartItemDto cartItemDto);

    List<CartItemDto> getCartItemsByLoggedUser();

    CartItemDto getMyCartItem(Long cartItemId);

    List<CartItemDto> getAllCartItems();

    CartItemDto updateCartItem(Long cartItemId, CartItemDto updatedCartItem);

    void removeFromCart(Long itemId);

}
