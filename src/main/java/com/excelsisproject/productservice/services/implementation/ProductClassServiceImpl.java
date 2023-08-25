package com.excelsisproject.productservice.services.implementation;

import com.excelsisproject.productservice.dto.ProductClassDto;
import com.excelsisproject.productservice.dto.ProductDto;
import com.excelsisproject.productservice.entities.Product;
import com.excelsisproject.productservice.entities.ProductClass;
import com.excelsisproject.productservice.exceptions.AppException;
import com.excelsisproject.productservice.exceptions.ResourceNotFoundException;
import com.excelsisproject.productservice.mappers.ProductMapper;
import com.excelsisproject.productservice.repositories.ProductClassRepository;
import com.excelsisproject.productservice.services.ProductClassService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductClassServiceImpl implements ProductClassService {

    private ProductClassRepository productClassRepository;

    @Override
    public ProductClassDto createProductClass(ProductClassDto productClassDto){

        ProductClass productClass = ProductMapper.mapToProductClass(productClassDto);
        List<Product> products = new ArrayList<>();
        productClass.setProducts(products);
        ProductClass savedProductClass = productClassRepository.save(productClass);
        return ProductMapper.mapToProductClassDto(savedProductClass);
    }

    @Override
    public ProductClassDto addProduct(ProductDto productDto) {
        ProductClass productClass  = productClassRepository.findById(productDto.getProductClassId()).orElseThrow(
                () -> new ResourceNotFoundException("Product class does not exists with given id: " + productDto.getId()));

        Product product = ProductMapper.mapToProduct(productDto);
        productClass.getProducts().add(product);
        ProductClass savedProductClass = productClassRepository.save(productClass);

        return ProductMapper.mapToProductClassDto(savedProductClass);
    }

    @Override
    public ProductClassDto getProductClassById(Long productClassId){
        ProductClass productClass = productClassRepository.findById(productClassId).orElseThrow(
                () -> new ResourceNotFoundException("Product class does not exists with given id: " + productClassId));

        return ProductMapper.mapToProductClassDto(productClass);
    }

    @Override
    public List<ProductClassDto> getAllProductClasses() {
        List<ProductClass> productClasses = productClassRepository.findAll();

        return productClasses.stream().map(ProductMapper::mapToProductClassDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductClassDto updateProductClass(Long productClassId, ProductClassDto updatedProductClass) {
        ProductClass productClass = productClassRepository.findById(productClassId).orElseThrow(
                ()-> new ResourceNotFoundException("Product class does not exist with given id: " + productClassId));

        productClass.setDescription(updatedProductClass.getDescription());

        ProductClass updatedProductClassObj = productClassRepository.save(productClass);

        return ProductMapper.mapToProductClassDto(updatedProductClassObj);
    }

    @Override
    public void deleteProductClass(Long productClassId) {
        ProductClass productClass = productClassRepository.findById(productClassId).orElseThrow(
                ()-> new ResourceNotFoundException("Product class does not exist with given id: " + productClassId));

        productClassRepository.deleteById(productClassId);
    }
}
