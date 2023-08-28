package com.excelsisproject.productservice.mappers;

import com.excelsisproject.productservice.dto.ProductClassDto;
import com.excelsisproject.productservice.dto.ProductDto;
import com.excelsisproject.productservice.entities.Product;
import com.excelsisproject.productservice.entities.ProductClass;
import com.excelsisproject.productservice.repositories.ProductClassRepository;
import com.excelsisproject.productservice.services.ProductClassService;
import com.excelsisproject.productservice.services.ProductService;

public class ProductMapper {
    private static ProductClassService productClassService;

    public static ProductDto mapToProductDto(Product product){
        return new ProductDto(
                product.getId(),
                product.getColor(),
                product.getAmountInStock(),
                product.getImageFiles(),
                product.getProductClassId()
        );
    }

    public static Product mapToProduct(ProductDto productDto){
        return new Product(
                productDto.getId(),
                productDto.getColor(),
                productDto.getAmountInStock(),
                productDto.getImageFiles(),
                productDto.getProductClassId()
        );
    }


    public static ProductClassDto mapToProductClassDto(ProductClass productClass){
        return new ProductClassDto(
                productClass.getProductClassId(),
                productClass.getName(),
                productClass.getDescription(),
                productClass.getCategory(),
                productClass.getProducts(),
                productClass.getPrice(),
                productClass.getImageFiles()
        );
    }

    public static ProductClass mapToProductClass(ProductClassDto productClassDto){
        return new ProductClass(
                productClassDto.getProductClassId(),
                productClassDto.getName(),
                productClassDto.getDescription(),
                productClassDto.getCategory(),
                productClassDto.getProducts(),
                productClassDto.getPrice(),
                productClassDto.getImageFiles()
        );
    }
}
