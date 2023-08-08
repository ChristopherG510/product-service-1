package com.excelsisproject.productservice.controllers;

import com.excelsisproject.productservice.dto.CartItemDto;
import com.excelsisproject.productservice.dto.ProductDto;
import com.excelsisproject.productservice.entities.ImageModel;
import com.excelsisproject.productservice.services.CartService;
import com.excelsisproject.productservice.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private CartService cartService;

    // Add Product
    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = {"/createNew"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
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

    // Get product by id
    //@PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    @GetMapping("/view/productId/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long productId){
        ProductDto productDto = productService.getProductById(productId);
        return ResponseEntity.ok(productDto);
    }

    // Get all products
    //@PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    @GetMapping("/view/all")
    public ResponseEntity<List<ProductDto>> getAllProducts(@RequestParam(defaultValue = "0") int pageNumber){
        List<ProductDto> products = productService.getAllProducts(pageNumber);
        return ResponseEntity.ok(products);
    }

    // Update product
    //@PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/edit/productId/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("id") Long productId, @RequestBody ProductDto updatedProduct){
        ProductDto productDto = productService.updateProduct(productId, updatedProduct);
        return ResponseEntity.ok(productDto);
    }

    // Delete product
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/productId/{id}")
    public void deleteProduct(@PathVariable("id") Long productId){
        productService.deleteProduct(productId);
    }

    // search products
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> searchProducts(@RequestParam String searchKey){
        List<ProductDto> products = productService.searchProducts(searchKey);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ProductDto>> filterProducts(@RequestParam String filter, String field, String sortParam, String direction, int page){
        List<ProductDto> products = productService.filterProducts(filter, field,sortParam, direction, page);
        return ResponseEntity.ok(products);
    }

    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    @PostMapping("/addToCart")
    public ResponseEntity<CartItemDto> addProductToCart(@RequestBody CartItemDto cartItem){
        cartService.addToCart(cartItem);
        return ResponseEntity.ok(cartItem);
    }

    public Set<ImageModel> uploadImage(MultipartFile[] multipartFiles) throws IOException{
        Set<ImageModel> imageModels = new HashSet<>();

        for (MultipartFile file: multipartFiles){
            ImageModel imageModel = new ImageModel(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
            );
            imageModels.add(imageModel);
        }
        return imageModels;
    }
}
