package com.excelsisproject.productservice.controllers;

import com.excelsisproject.productservice.dto.CartItemDto;
import com.excelsisproject.productservice.services.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private CartService cartService;

    @GetMapping("/miCarrito")
    public ResponseEntity<List<CartItemDto>> getAllMyCartItems(){

        List<CartItemDto> cartItems = cartService.getAllMyCartItems();

        return ResponseEntity.ok(cartItems);
    }

    @GetMapping("/admin/allCartItems")
    public ResponseEntity<List<CartItemDto>> getAllCartItems(){

        List<CartItemDto> cartItems = cartService.getAllCartItems();

        return ResponseEntity.ok(cartItems);
    }

    @PutMapping("/miCarrito/edit/{id}")
    public ResponseEntity<CartItemDto> updateCartItem(@PathVariable("id") Long cartItemId, @RequestBody CartItemDto updatedCartItem){

        CartItemDto cartItem = cartService.updateCartItem(cartItemId, updatedCartItem);

        return ResponseEntity.ok(cartItem);
    }

    @DeleteMapping("/miCarrito/delete/{id}")
    public String removeFromCart(@PathVariable("id") Long cartItemId){

        CartItemDto cartItem = cartService.getMyCartItem(cartItemId);

        if (cartItem != null){
            cartService.removeFromCart(cartItemId);
            return "item with id: " + cartItemId +" deleted from cart";
        } else {
            return "item with id: " + cartItemId + "does not exist in cart";
        }
    }

}
