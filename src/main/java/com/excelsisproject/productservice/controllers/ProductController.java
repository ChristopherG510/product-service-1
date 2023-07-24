package com.excelsisproject.productservice.controllers;

import com.excelsisproject.productservice.dto.ProductDto;
import com.excelsisproject.productservice.entities.ImageModel;
import com.excelsisproject.productservice.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private ProductService productService;

    // Add Product
    @PostMapping(value = {""}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ProductDto> createProduct(@RequestPart("product") ProductDto productDto, @RequestPart(value = "imageFile", required = false) MultipartFile[] file) {
        try{
            Set<ImageModel> images = uploadImage(file);
            productDto.setImageFiles(images);
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
        ProductDto savedProduct = productService.createProduct(productDto);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    public Set<ImageModel> uploadImage(MultipartFile[] multipartFiles) throws IOException{
        Set<ImageModel> imageModels = new HashSet<>();

        for (MultipartFile file: multipartFiles){
            ImageModel imageModel = new ImageModel(null,
                    file.getBytes()
            );
            imageModels.add(imageModel);
        }
        return imageModels;
    }

    // Get product by id
    @GetMapping("{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long productId){
        ProductDto productDto = productService.getProductById(productId);
        return ResponseEntity.ok(productDto);
    }

    // Get all products
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts(){
        List<ProductDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // Update product
    @PutMapping("{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("id") Long productId, @RequestBody ProductDto updatedProduct){
        ProductDto productDto = productService.updateProduct(productId, updatedProduct);
        return ResponseEntity.ok(productDto);
    }

    // Delete product
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long productId){
        productService.deleteProduct(productId);
        return ResponseEntity.ok("Product deleted.");
    }

    // search products
    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> searchProducts(@RequestParam String searchKey){
        List<ProductDto> products = productService.searchProducts(searchKey);
        return ResponseEntity.ok(products);
    }

}
