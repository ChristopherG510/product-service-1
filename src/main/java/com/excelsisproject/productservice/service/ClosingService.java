package com.excelsisproject.productservice.service;

import com.excelsisproject.productservice.dto.ClosingDetailsDto;
import com.excelsisproject.productservice.dto.OrderDto;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ClosingService {

    ClosingDetailsDto createDetails(ClosingDetailsDto closingDetailsDto);

    ClosingDetailsDto getDetailsById(Long detailsId);

    List<ClosingDetailsDto> getAllDetails();

    ClosingDetailsDto updateDetails(Long detailsId, ClosingDetailsDto updatedDetails);


}
