package com.excelsisproject.productservice.services.implementation;

import com.excelsisproject.productservice.dto.CartItemDto;
import com.excelsisproject.productservice.dto.ProductDto;
import com.excelsisproject.productservice.entities.CartItem;
import com.excelsisproject.productservice.entities.Product;
import com.excelsisproject.productservice.exceptions.ResourceNotFoundException;
import com.excelsisproject.productservice.mappers.CartItemMapper;
import com.excelsisproject.productservice.repositories.CartRepository;
import com.excelsisproject.productservice.repositories.UserRepository;
import com.excelsisproject.productservice.services.CartService;
import com.excelsisproject.productservice.services.ProductService;
import com.excelsisproject.productservice.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {

    private ProductService productService;
    private UserRepository userRepository;
    private UserService userService;
    private CartRepository cartRepository;
    private static final String ORDER_IN_CART ="In cart";

    @Override
    public CartItemDto addToCart(CartItemDto cartItemDto) {

        ProductDto product = productService.getProductById(cartItemDto.getProductId());

        if(product.getAmountInStock() >= cartItemDto.getAmount()) {
            cartItemDto.setProductName(product.getName());
            cartItemDto.setStatus(ORDER_IN_CART);
            cartItemDto.setUserId(userService.getLoggedUserId());
            cartItemDto.setPrice(cartItemDto.getAmount() * product.getPrice());

            CartItem cartItem = CartItemMapper.mapToCartItem(cartItemDto);
            CartItem savedCartItem = cartRepository.save(cartItem);

            return CartItemMapper.mapToCartItemDto(savedCartItem);
        } else {
            return null;
        }

    }

    @Override
    public List<CartItemDto> getCartItemsByLoggedUser() {
        List<CartItem> cartItemList = cartRepository.findAllByUserId(userService.getLoggedUserId());

        return cartItemList.stream().map((cartItem) -> CartItemMapper.mapToCartItemDto(cartItem))
                .collect(Collectors.toList());
    }

    @Override
    public CartItemDto getMyCartItem(Long cartItemId) {
        CartItem cartItem = cartRepository.findById(cartItemId).orElseThrow(
                ()-> new ResourceNotFoundException("Cart item does not exist with given id: " + cartItemId));

        if (Objects.equals(cartItem.getUserId(), userService.getLoggedUserId())){
            return CartItemMapper.mapToCartItemDto(cartItem);
        } else {
            return null;
        }
    }

    @Override
    public List<CartItemDto> getAllCartItems() {
        List<CartItem> cartItemList = cartRepository.findAll();

        return cartItemList.stream().map((cartItem) -> CartItemMapper.mapToCartItemDto(cartItem))
                .collect(Collectors.toList());
    }

    @Override
    public List<CartItemDto> getAllMyCartItems() {
        List<CartItem> cartItemList = cartRepository.findAllByUserIdAndStatus(userService.getLoggedUserId(), "In cart");

        return cartItemList.stream().map((cartItem) -> CartItemMapper.mapToCartItemDto(cartItem))
                .collect(Collectors.toList());
    }

    @Override
    public CartItemDto updateCartItem(Long cartItemId, CartItemDto updatedCartItem) {
        CartItem cartItem = cartRepository.findById(cartItemId).orElseThrow(
                ()-> new ResourceNotFoundException("Cart item does not exist with given id: " + cartItemId));

        if(cartItem.getUserId() == userService.getLoggedUserId()) {
            ProductDto product = productService.getProductById(cartItem.getProductId());
            cartItem.setAmount(updatedCartItem.getAmount());
            cartItem.setPrice(updatedCartItem.getAmount() * product.getPrice());

            CartItem updatedCartItemObj = cartRepository.save(cartItem);

            return CartItemMapper.mapToCartItemDto(updatedCartItemObj);
        } else {
return null;
        }
    }

    @Override
    public void removeFromCart(Long itemId) {
        CartItem cartItem = cartRepository.findById(itemId).orElseThrow(
                ()-> new ResourceNotFoundException("Item does not exist with given id: " + itemId));

        cartRepository.deleteById(itemId);
    }

}