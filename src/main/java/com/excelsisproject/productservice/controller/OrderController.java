package com.excelsisproject.productservice.controller;

import com.excelsisproject.productservice.dto.OrderDto;
import com.excelsisproject.productservice.dto.ProductDto;
import com.excelsisproject.productservice.service.OrderService;
import com.excelsisproject.productservice.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private OrderService orderService;
    private ProductService productService;

    // create order
    @PostMapping
    public ResponseEntity<OrderDto> orderProduct(@RequestBody OrderDto orderDto){
        OrderDto savedOrder = orderService.orderProduct(orderDto);
        double amountOrdered = orderDto.getOrderAmount();
        Long productId = orderDto.getProductId();

        ProductDto productDto = productService.updateAmount(productId, amountOrdered);
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }

    // get order
    @GetMapping("{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable("id") Long orderId){
        OrderDto orderDto = orderService.getOrderById(orderId);
        return ResponseEntity.ok(orderDto);
    }

    // get all orders
    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders(){
        List<OrderDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // Update order
    @PutMapping("{id}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable("id") Long orderId, @RequestBody OrderDto updatedOrder){
        OrderDto orderDto = orderService.updateOrder(orderId, updatedOrder);
        return ResponseEntity.ok(orderDto);
    }

}
