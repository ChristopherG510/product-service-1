package com.excelsisproject.productservice.mapper;

import com.excelsisproject.productservice.dto.ClosingDetailsDto;
import com.excelsisproject.productservice.entity.ClosingDetails;

public class ClosingDetailsMapper {

    public static ClosingDetailsDto mapToClosingDetailsDto(ClosingDetails closingDetails){
        return new ClosingDetailsDto(
                closingDetails.getId(),
                closingDetails.getDate(),
                closingDetails.getTotalOrders(),
                closingDetails.getTotalAmountSold()
        );
    }


    public static ClosingDetails mapToClosingDetails(ClosingDetailsDto closingDetailsDto){
        return new ClosingDetails(
                closingDetailsDto.getId(),
                closingDetailsDto.getDate(),
                closingDetailsDto.getTotalOrders(),
                closingDetailsDto.getTotalAmountSold()
        );
    }
}
