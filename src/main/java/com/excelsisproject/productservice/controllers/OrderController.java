package com.excelsisproject.productservice.controllers;

import com.excelsisproject.productservice.dto.FacturaDto;
import com.excelsisproject.productservice.dto.OrderDto;
import com.excelsisproject.productservice.dto.RequestDto;
import com.excelsisproject.productservice.services.FacturaService;
import com.excelsisproject.productservice.services.OrderService;
import lombok.AllArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private OrderService orderService;
    private FacturaService facturaService;

    // create order
    //@PreAuthorize("hasAuthority('CLIENTE') or hasAuthority('ADMIN')")
    @PostMapping("/order")
    public OrderDto orderProduct(@RequestBody OrderDto orderDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUser = authentication.getName();
        System.out.println("Usuario Logueado: " + loggedUser);
        return orderService.orderProduct(orderDto, loggedUser);
    }

    // get order
    //@PreAuthorize("hasAuthority('CLIENTE') or hasAuthority('ADMIN')")
    @GetMapping("/view/orderId/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable("id") Long orderId){
        OrderDto orderDto = orderService.getOrderById(orderId);
        return ResponseEntity.ok(orderDto);
    }

    // get all orders
    //@PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/viewAll")
    public ResponseEntity<List<OrderDto>> getAllOrders(){
        List<OrderDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    //@PreAuthorize("hasAuthority('CLIENTE') or hasAuthority('ADMIN')")
    @GetMapping("/view/myOrders")
    public ResponseEntity<List<OrderDto>> getOrdersByUser(){
        List<OrderDto> orderDto = orderService.getOrdersByUser();
        return ResponseEntity.ok(orderDto);
    }

    // Update order
    //@PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/edit/orderId/{id}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable("id") Long orderId, @RequestBody OrderDto updatedOrder){
        OrderDto orderDto = orderService.updateOrder(orderId, updatedOrder);
        return ResponseEntity.ok(orderDto);
    }

    //@PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/orderId/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable("id") Long orderId){
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok("Order deleted.");
    }

    @PutMapping("/facturar")
    public ResponseEntity<FacturaDto> facturarOrden(@RequestBody FacturaDto facturaDto, Long orderId){
        FacturaDto savedFactura = facturaService.facturar(facturaDto, orderId);

        return ResponseEntity.ok(savedFactura);
    }

    @GetMapping("/misFacturas")
    public ResponseEntity<List<FacturaDto>> getMisFacturas(){
        List<FacturaDto> facturaDto = facturaService.getMisFacturas();

        return ResponseEntity.ok(facturaDto);
    }

    @GetMapping("/facturas")
    public ResponseEntity<List<FacturaDto>> getAllFacturas(){
        List<FacturaDto> facturaDto = facturaService.getAllFacturas();

        return ResponseEntity.ok(facturaDto);
    }

    @PostMapping("/filterOrders")
    public ResponseEntity<List<OrderDto>> orderFilter(@RequestBody RequestDto requestDto){
        List<OrderDto> orders = orderService.orderFilter(requestDto);
        return ResponseEntity.ok(orders);
    }
}