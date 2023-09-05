package com.excelsisproject.productservice.services.implementation;

import com.excelsisproject.productservice.dto.PageRequestDto;
import com.excelsisproject.productservice.dto.ProductDto;
import com.excelsisproject.productservice.dto.RequestDto;
import com.excelsisproject.productservice.entities.ImageModel;
import com.excelsisproject.productservice.entities.Product;
import com.excelsisproject.productservice.exceptions.AppException;
import com.excelsisproject.productservice.exceptions.ResourceNotFoundException;
import com.excelsisproject.productservice.mappers.ProductMapper;
import com.excelsisproject.productservice.repositories.ProductRepository;
import com.excelsisproject.productservice.services.FilterSpecification;
import com.excelsisproject.productservice.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private FilterSpecification<Product> productFilterSpecification;

    @Override
    public ProductDto createProduct(ProductDto productDto){

        Optional<Product> verif = productRepository.getByColorAndProductClassId(productDto.getColor(), productDto.getProductClassId());
        if (verif.isPresent()){
            throw new AppException("Color de producto ya existe.", HttpStatus.CONFLICT);
        }

        Product product = ProductMapper.mapToProduct(productDto);
        Product savedProduct = productRepository.save(product);
        return ProductMapper.mapToProductDto(savedProduct);
    }

    @Override
    public ProductDto getProductById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Producto no existe con id: " + productId));

        return ProductMapper.mapToProductDto(product);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(ProductMapper::mapToProductDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getProductsByClass(Long classId) {
        List<Product> products = productRepository.getAllByProductClassId(classId);
        return products.stream().map(ProductMapper::mapToProductDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto updateProduct(Long productId, ProductDto updatedProduct) {
        Product product = productRepository.findById(productId).orElseThrow(
                ()-> new ResourceNotFoundException("Producto no existe con id: " + productId));
        product.setColor(updatedProduct.getColor());
        product.setAmountInStock(updatedProduct.getAmountInStock());
        product.setImageFiles(updatedProduct.getImageFiles());

        Product updatedProductObj = productRepository.save(product);

        return ProductMapper.mapToProductDto(updatedProductObj);
    }


    @Override
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                ()-> new ResourceNotFoundException("Producto no existe con id: " + productId));

        productRepository.deleteById(productId);
    }

    @Override
    public ProductDto updateStock(Long productId, double amountOrdered) {
        Product product = productRepository.findById(productId).orElseThrow(
                ()-> new ResourceNotFoundException("Producto no existe con id: " + productId));

        product.setAmountInStock(product.getAmountInStock() - amountOrdered);

        Product updatedProductObj = productRepository.save(product);

        return ProductMapper.mapToProductDto(updatedProductObj);
    }

    @Override
    public List<ProductDto> productsSpecification(RequestDto requestDto) {
        Specification<Product> searchSpecification = productFilterSpecification
                .getSearchSpecification(requestDto.getSearchRequestDto(), requestDto.getGlobalOperator());

        List<Product> products = productRepository.findAll(searchSpecification);
        return products.stream().map(ProductMapper::mapToProductDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> productFilter(RequestDto requestDto) {
        Specification<Product> searchSpecification = productFilterSpecification
                .getSearchSpecification(requestDto.getSearchRequestDto(), requestDto.getGlobalOperator());

        Pageable pageable = new PageRequestDto().getPageable(requestDto.getPageDto());
        System.out.println(pageable);

        Page<Product> products = productRepository.findAll(searchSpecification, pageable);
        return products.stream().map(ProductMapper::mapToProductDto)
                .collect(Collectors.toList());
    }
}

//   |  |l
//   || |_