package com.excelsisproject.productservice.services;

import com.excelsisproject.productservice.dto.ProductClassDto;
import com.excelsisproject.productservice.dto.ProductDto;

import java.util.List;

public interface ProductClassService {

    ProductClassDto createProductClass(ProductClassDto productClassDto);

    ProductClassDto addProduct(ProductDto productDto);

    ProductClassDto getProductClassById(Long productClassId);

    List<ProductClassDto> getAllProductClasses();

    ProductClassDto updateProductClass(Long productClassId, ProductClassDto updatedProductClass);

    void deleteProductClass(Long productClassId);
}
