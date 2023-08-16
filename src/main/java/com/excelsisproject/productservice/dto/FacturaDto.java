package com.excelsisproject.productservice.dto;

import com.excelsisproject.productservice.entities.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FacturaDto {

    private Long id;
    private Long userId;
    private String name;
    private String ruc;
    private Order order;

}
