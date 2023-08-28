package com.excelsisproject.productservice.controllers;

import com.excelsisproject.productservice.dto.ProductClassDto;
import com.excelsisproject.productservice.services.ProductClassService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductClassController {

    private ProductClassService productClassService;

    @PostMapping("/createProductClass")
    public ResponseEntity<ProductClassDto> createProductClass(@RequestBody ProductClassDto productClassDto){
        return ResponseEntity.ok(productClassService.createProductClass(productClassDto));
    }

    // Get la clase de producto
    @GetMapping("/view/Product/{id}")
    public ResponseEntity<ProductClassDto> getProductClassById(@PathVariable("id") Long productClassId){
        ProductClassDto productClass = productClassService.getProductClassById(productClassId);
        return ResponseEntity.ok(productClass);
    }

    // Get TODAS las CLASES de producto
    @GetMapping("/view/allProducts")
    public ResponseEntity<List<ProductClassDto>> getAllProductClasses(){
        List<ProductClassDto> productClasses = productClassService.getAllProductClasses();
        return ResponseEntity.ok(productClasses);
    }

    @PutMapping("/edit/productClassId/{id}")
    public ResponseEntity<ProductClassDto> updateProductClass(@PathVariable("id") Long productClassId, @RequestBody ProductClassDto updatedProductClass){
        ProductClassDto productClassDto = productClassService.updateProductClass(productClassId, updatedProductClass);
        return ResponseEntity.ok(productClassDto);
    }

    // Delete product
    //@PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/productClassId/{id}")
    public void deleteProductClass(@PathVariable("id") Long productClassId){
        productClassService.deleteProductClass(productClassId);
    }
}
