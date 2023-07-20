package com.excelsisproject.productservice.dto;

import com.excelsisproject.productservice.entity.ImageModel;
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
    private String name;
    private String description;
    private double amountInStock;
    private double price;
    Set<ImageModel> imageFiles;
}
