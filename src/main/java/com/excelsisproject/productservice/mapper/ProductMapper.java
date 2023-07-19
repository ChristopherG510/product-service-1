package com.excelsisproject.productservice.mapper;

import com.excelsisproject.productservice.dto.ProductDto;
import com.excelsisproject.productservice.entity.Product;

public class ProductMapper {

    public static ProductDto mapToProductDto(Product product){
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getAmountInStock(),
                product.getPrice()
        );
    }

    public static Product mapToProduct(ProductDto productDto){
        return new Product(
                productDto.getId(),
                productDto.getName(),
                productDto.getDescription(),
                productDto.getAmountInStock(),
                productDto.getPrice()
        );
    }
}
