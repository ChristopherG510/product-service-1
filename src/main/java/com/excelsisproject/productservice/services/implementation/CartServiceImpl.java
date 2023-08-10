package com.excelsisproject.productservice.services.implementation;

import com.excelsisproject.productservice.dto.CartItemDto;
import com.excelsisproject.productservice.dto.ProductDto;
import com.excelsisproject.productservice.entities.CartItem;
import com.excelsisproject.productservice.exceptions.AppException;
import com.excelsisproject.productservice.exceptions.ResourceNotFoundException;
import com.excelsisproject.productservice.mappers.CartItemMapper;
import com.excelsisproject.productservice.repositories.CartRepository;
import com.excelsisproject.productservice.repositories.UserRepository;
import com.excelsisproject.productservice.services.CartService;
import com.excelsisproject.productservice.services.ProductService;
import com.excelsisproject.productservice.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {

    private ProductService productService;
    private UserService userService;
    private CartRepository cartRepository;
    private static final String ORDER_IN_CART ="In cart";

    @Override
    public CartItemDto addToCart(CartItemDto cartItemDto) {

        ProductDto product = productService.getProductById(cartItemDto.getProductId());
        List<CartItem> otherItemsInCart = cartRepository.findAllByUserIdAndStatus(userService.getLoggedUserId(), ORDER_IN_CART);

        // Iterar en la lista de items del carrito para verificar si el producto ya existe en el carrito
        for (CartItem cartItem : otherItemsInCart){
            // verificar si ya existe el producto en el carrito
            if(Objects.equals(cartItem.getProductId(), cartItemDto.getProductId())){
                // si hay mas productos en el carrito que en el stock se tira una excepcion, y no se agrega al carrito
                if(product.getAmountInStock() < cartItemDto.getAmount() + cartItem.getAmount()) {
                    throw new AppException("No existen suficientes productos en stock", HttpStatus.BAD_REQUEST);
                }
                // Agregar la cantidad del producto al cartItem
                cartItem.setAmount(cartItem.getAmount() + cartItemDto.getAmount());
                cartItem.setPrice(cartItem.getAmount() * product.getPrice());

                CartItem savedCartItem = cartRepository.save(cartItem);
                return CartItemMapper.mapToCartItemDto(savedCartItem);
            }
        }

        // Si el producto no esta en el carrito, se crea un nuevo cartItem
        if(product.getAmountInStock() >= cartItemDto.getAmount()) {
            cartItemDto.setProductName(product.getName());
            cartItemDto.setStatus(ORDER_IN_CART);
            cartItemDto.setUserId(userService.getLoggedUserId());
            cartItemDto.setPrice(cartItemDto.getAmount() * product.getPrice());

            CartItem cartItem = CartItemMapper.mapToCartItem(cartItemDto);
            CartItem savedCartItem = cartRepository.save(cartItem);

            return CartItemMapper.mapToCartItemDto(savedCartItem);
        } else {
            // Si no hay suficientes productos en stock, se tira excepcion
            throw new AppException("No existen suficientes productos en stock", HttpStatus.BAD_REQUEST);
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
                ()-> new AppException("No existe item de carrito con id: "+ cartItemId, HttpStatus.NOT_FOUND));

        if (Objects.equals(cartItem.getUserId(), userService.getLoggedUserId())){
            return CartItemMapper.mapToCartItemDto(cartItem);
        } else {
            throw new AppException("No existe el item en su carrito", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public double getMyCartPrice() {    // Suma el precio total del carrito para el checkout
        List<CartItem> cartItemList = cartRepository.findAllByUserIdAndStatus(userService.getLoggedUserId(), "In cart");
        double price = 0;
        for (CartItem cartItem : cartItemList){
            price += cartItem.getPrice();
        }
        return price;
    }


    @Override
    public List<CartItemDto> getAllCartItems() {    // Trae todos los items de carrito para el Admin
        List<CartItem> cartItemList = cartRepository.findAll();

        return cartItemList.stream().map((cartItem) -> CartItemMapper.mapToCartItemDto(cartItem))
                .collect(Collectors.toList());
    }

    @Override
    public List<CartItemDto> getAllMyCartItems() {  // Trae los items del carrito del usuario
        List<CartItem> cartItemList = cartRepository.findAllByUserIdAndStatus(userService.getLoggedUserId(), "In cart");

        return cartItemList.stream().map((cartItem) -> CartItemMapper.mapToCartItemDto(cartItem))
                .collect(Collectors.toList());
    }

    @Override
    public CartItemDto updateCartItem(Long cartItemId, CartItemDto updatedCartItem) {
        CartItem cartItem = cartRepository.findById(cartItemId).orElseThrow(
                ()-> new AppException("No existe item de carrito con id: "+ cartItemId, HttpStatus.NOT_FOUND));

        if(cartItem.getUserId() == userService.getLoggedUserId()) { // Si la id del usuario no coincide con la del item de carrito, no puede editarlo
            ProductDto product = productService.getProductById(cartItem.getProductId());
            cartItem.setAmount(updatedCartItem.getAmount());
            cartItem.setPrice(updatedCartItem.getAmount() * product.getPrice());

            CartItem updatedCartItemObj = cartRepository.save(cartItem);

            return CartItemMapper.mapToCartItemDto(updatedCartItemObj);
        } else {
            throw  new AppException("Item does not exist cart of user", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void removeFromCart(Long itemId) {
        CartItem cartItem = cartRepository.findById(itemId).orElseThrow(
                ()-> new AppException("No existe item de carrito con id: "+ itemId, HttpStatus.NOT_FOUND));

        cartRepository.deleteById(itemId);
    }

}