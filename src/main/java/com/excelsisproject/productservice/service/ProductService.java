package com.excelsisproject.productservice.service;

import com.excelsisproject.productservice.dto.ProductDto;

import java.util.List;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto); // Crear un producto

    ProductDto getProductById(Long productId); // Obtener un producto por id

    List<ProductDto> getAllProducts(); // Obtener todos los productos

    ProductDto updateProduct(Long productId, ProductDto updatedProduct); // Modificar un producto

    void deleteProduct(Long productId); // Eliminar un producto

//    ProductDto orderProduct(Long productId, ProductDto orderedProduct);

}
