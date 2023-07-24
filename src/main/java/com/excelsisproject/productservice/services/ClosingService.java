package com.excelsisproject.productservice.services;

import com.excelsisproject.productservice.dto.ClosingDetailsDto;

import java.util.List;


public interface ClosingService {

    ClosingDetailsDto createDetails(ClosingDetailsDto closingDetailsDto);

    ClosingDetailsDto getDetailsById(Long detailsId);

    List<ClosingDetailsDto> getAllDetails();

    ClosingDetailsDto updateDetails(Long detailsId, ClosingDetailsDto updatedDetails);


}
