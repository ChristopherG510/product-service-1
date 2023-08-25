package com.excelsisproject.productservice.dto;

import com.excelsisproject.productservice.entities.ImageModel;
import com.excelsisproject.productservice.entities.ProductClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;
    private String color;
    private double amountInStock;
    private Set<ImageModel> imageFiles;
    private Long productClassId;
}
