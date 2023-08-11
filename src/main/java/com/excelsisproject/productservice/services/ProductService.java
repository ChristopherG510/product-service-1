package com.excelsisproject.productservice.services;

import com.excelsisproject.productservice.dto.CartItemDto;
import com.excelsisproject.productservice.dto.ProductDto;
import com.excelsisproject.productservice.dto.RequestDto;

import java.util.List;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto); // Crear un producto

    ProductDto getProductById(Long productId); // Obtener un producto por id

    List<ProductDto> getAllProducts(); // Obtener todos los productos

    ProductDto updateProduct(Long productId, ProductDto updatedProduct); // Modificar un producto

    void deleteProduct(Long productId); // Eliminar un producto

    ProductDto updateStock(Long productId, double amountOrdered);

    double getPrice(Long productId);

    List<ProductDto> searchProducts(String searchKey);

    List<ProductDto> filterProducts(String filter, String field, String sortParam, String direction, int page, int pageSize);

    List<ProductDto> productsSpecification(RequestDto requestDto);

    List<ProductDto> productFilter(RequestDto requestDto);

}
