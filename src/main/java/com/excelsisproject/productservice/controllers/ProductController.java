package com.excelsisproject.productservice.controllers;

import com.excelsisproject.productservice.dto.CartItemDto;
import com.excelsisproject.productservice.dto.ProductClassDto;
import com.excelsisproject.productservice.dto.ProductDto;
import com.excelsisproject.productservice.dto.RequestDto;
import com.excelsisproject.productservice.entities.ImageModel;
import com.excelsisproject.productservice.services.CartService;
import com.excelsisproject.productservice.services.ProductClassService;
import com.excelsisproject.productservice.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
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
    private ProductClassService productClassService;
    private CartService cartService;

    // Add Product
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = {"/createNew/{productClassId}"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ProductClassDto> createProduct(@PathVariable("productClassId") Long id, @RequestPart("product") ProductDto productDto, @RequestPart(value = "imageFile", required = false) MultipartFile[] file) {
        try{
            Set<ImageModel> images = uploadImage(file);
            productDto.setImageFiles(images);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        productDto.setProductClassId(id);
        ProductClassDto savedProduct = productClassService.addProduct(productDto);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }


    @PostMapping("/addProduct")
    public ResponseEntity<ProductClassDto> addProduct(@RequestBody ProductDto productDto){
        return ResponseEntity.ok(productClassService.addProduct(productDto));
    }

    // Get variacion de producto
    // El que vamos a usar para seleccionar el estock
    //@PreAuthorize("hasAuthority('CLIENTE') or hasAuthority('ADMIN')")
    @GetMapping("/view/productId/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long productId){
        ProductDto productDto = productService.getProductById(productId);
        return ResponseEntity.ok(productDto);
    }

    // Get todas las variaciones de los producto ESTE NO VAMO A USAR
    //@PreAuthorize("hasAuthority('CLIENTE') or hasAuthority('ADMIN')")
    @GetMapping("/view/all")
    public ResponseEntity<List<ProductDto>> getAllProducts(){
        List<ProductDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // Get las variaciones por clase de producto (mandar la id de la clase)
    // El que vamos a usar para ver todas las variaciones de la clase de producto
    @GetMapping("/view/productByClass/{classId}")
    public ResponseEntity<List<ProductDto>> getProductByClass(@PathVariable("classId") Long productClassId){
        List<ProductDto> products = productService.getProductsByClass(productClassId);
        return ResponseEntity.ok(products);
    }

    // Update product
    //@PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/edit/productId/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("id") Long productId, @RequestPart("product") ProductDto updatedProduct, @RequestPart(value = "imageFile", required = false) MultipartFile[] file){
        try{
            Set<ImageModel> images = uploadImage(file);
            updatedProduct.setImageFiles(images);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        ProductDto productDto = productService.updateProduct(productId, updatedProduct);
        return ResponseEntity.ok(productDto);
    }

    // Delete product
    //@PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/productId/{id}")
    public void deleteProduct(@PathVariable("id") Long productId){
        productService.deleteProduct(productId);
    }

    //@PreAuthorize("hasAuthority('CLIENTE') or hasAuthority('ADMIN')")
    @PostMapping("/addToCart")
    public ResponseEntity<CartItemDto> addProductToCart(@RequestBody CartItemDto cartItem){
        cartService.addToCart(cartItem);
        return ResponseEntity.ok(cartItem);
    }

    @PostMapping("/filterProducts")
    public ResponseEntity<List<ProductDto>> productFilter(@RequestBody RequestDto requestDto){
        List<ProductDto> products = productService.productFilter(requestDto);
        return ResponseEntity.ok(products);
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
