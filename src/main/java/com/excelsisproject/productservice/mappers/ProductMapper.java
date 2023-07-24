package com.excelsisproject.productservice.mappers;

import com.excelsisproject.productservice.dto.ProductDto;
import com.excelsisproject.productservice.entities.Product;

public class ProductMapper {
    public static ProductDto mapToProductDto(Product product){
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getAmountInStock(),
                product.getPrice(),
                product.getImageFiles()
        );
    }

    public static Product mapToProduct(ProductDto productDto){
        return new Product(
                productDto.getId(),
                productDto.getName(),
                productDto.getDescription(),
                productDto.getAmountInStock(),
                productDto.getPrice(),
                productDto.getImageFiles()
        );
    }
}
