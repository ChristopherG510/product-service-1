package com.excelsisproject.productservice.dto;

import com.excelsisproject.productservice.entities.ImageModel;
import com.excelsisproject.productservice.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductClassDto {

    private Long productClassId;
    private String name;
    private String description;
    private String category;
    private List<Product> products;
    private double price;
    private Set<ImageModel> imageFiles;
}
