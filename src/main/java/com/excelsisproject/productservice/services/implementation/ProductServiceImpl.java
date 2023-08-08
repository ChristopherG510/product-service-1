package com.excelsisproject.productservice.services.implementation;

import com.excelsisproject.productservice.dto.ProductDto;
import com.excelsisproject.productservice.entities.Product;
import com.excelsisproject.productservice.exceptions.ResourceNotFoundException;
import com.excelsisproject.productservice.mappers.ProductMapper;
import com.excelsisproject.productservice.repositories.ProductRepository;
import com.excelsisproject.productservice.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Override
    public ProductDto createProduct(ProductDto productDto) {

        Product product = ProductMapper.mapToProduct(productDto);
        Product savedProduct = productRepository.save(product);
        return ProductMapper.mapToProductDto(savedProduct);
    }

    @Override
    public ProductDto getProductById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product does not exists with given id: " + productId));

        return ProductMapper.mapToProductDto(product);
    }

    @Override
    public List<ProductDto> getAllProducts(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber,9);
        Page<Product> products = productRepository.findAll(pageable);

        return products.stream().map((product) -> ProductMapper.mapToProductDto(product))
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto updateProduct(Long productId, ProductDto updatedProduct) {
        Product product = productRepository.findById(productId).orElseThrow(
                ()-> new ResourceNotFoundException("Product does not exist with given id: " + productId));

        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        product.setAmountInStock(updatedProduct.getAmountInStock());
        product.setPrice(updatedProduct.getPrice());

        Product updatedProductObj = productRepository.save(product);

        return ProductMapper.mapToProductDto(updatedProductObj);
    }


    @Override
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                ()-> new ResourceNotFoundException("Product does not exist with given id: " + productId));

        productRepository.deleteById(productId);
    }

    @Override
    public ProductDto updateStock(Long productId, double amountOrdered) {
        Product product = productRepository.findById(productId).orElseThrow(
                ()-> new ResourceNotFoundException("Product does not exist with given id: " + productId));

        product.setAmountInStock(product.getAmountInStock() - amountOrdered);

        Product updatedProductObj = productRepository.save(product);

        return ProductMapper.mapToProductDto(updatedProductObj);
    }

    @Override
    public double getPrice(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product does not exists with given id: " + productId));
        return product.getPrice();
    }

    @Override
    public List<ProductDto> searchProducts(String searchKey) {
        List<Product> products = productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchKey, searchKey);
        return products.stream().map((product) -> ProductMapper.mapToProductDto(product))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> filterProducts(String filter, String field, String sortParam,String direction, int page) {
        Pageable pageable;

        if (Objects.equals(direction, "desc")){
            pageable = PageRequest.of(page,5, Sort.by(sortParam).descending());
        } else {
            pageable = PageRequest.of(page,5, Sort.by(sortParam).ascending());
        }
        Page<Product> products;

//        switch (filter){
//            case "name":
//                products = productRepository.findAllByNameContainingIgnoreCase(field, pageable);
//            case "price":
//                products = productRepository.findAllByPrice(Double.parseDouble(field), pageable);
//            default:
//                products = productRepository.findAll(pageable);
//        }
        if (Objects.equals(filter, "name")){
            products = productRepository.findAllByNameContainingIgnoreCase(field, pageable);
        } else if (Objects.equals(filter, "price")) {
            products = productRepository.findAllByPrice(Double.parseDouble(field), pageable);
        } else {
            products = productRepository.findAll(pageable);
        }
        //products = productRepository.findAll(pageable);

        return products.stream().map(ProductMapper::mapToProductDto)
                .collect(Collectors.toList());
    }
}
