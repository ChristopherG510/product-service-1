package com.excelsisproject.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClosingDetailsDto {

    private Long id;
    private String date;
    private double totalOrders;
    private double totalAmountSold;
}
