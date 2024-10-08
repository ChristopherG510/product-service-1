package com.excelsisproject.productservice.services.implementation;

import com.excelsisproject.productservice.dto.ClosingDetailsDto;
import com.excelsisproject.productservice.entities.ClosingDetails;
import com.excelsisproject.productservice.exceptions.ResourceNotFoundException;
import com.excelsisproject.productservice.mappers.ClosingDetailsMapper;
import com.excelsisproject.productservice.repositories.ClosingDetailsRepository;
import com.excelsisproject.productservice.repositories.OrderRepository;
import com.excelsisproject.productservice.services.ClosingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ClosingServiceImpl implements ClosingService {

    private ClosingDetailsRepository closingDetailsRepository;
    private OrderRepository orderRepository;

    @Override
    public ClosingDetailsDto createDetails(ClosingDetailsDto closingDetailsDto) {
        ClosingDetails details = ClosingDetailsMapper.mapToClosingDetails(closingDetailsDto);
        ClosingDetails savedDetails = closingDetailsRepository.save(details);

        return ClosingDetailsMapper.mapToClosingDetailsDto(savedDetails);
    }

    @Override
    public ClosingDetailsDto getDetailsById(Long detailsId) {
        ClosingDetails details= closingDetailsRepository.findById(detailsId).orElseThrow(
                () -> new ResourceNotFoundException("Closing details do not exist with given id: " + detailsId));

        return ClosingDetailsMapper.mapToClosingDetailsDto(details);
    }

    @Override
    public List<ClosingDetailsDto> getAllDetails() {
        List<ClosingDetails> closingDetails = closingDetailsRepository.findAll();
        return closingDetails.stream().map((closingDetail) -> ClosingDetailsMapper.mapToClosingDetailsDto(closingDetail))
                .collect(Collectors.toList());

    }

    @Override
    public ClosingDetailsDto updateDetails(Long detailsId, ClosingDetailsDto updatedDetails) {
        ClosingDetails details = closingDetailsRepository.findById(detailsId).orElseThrow(
                ()-> new ResourceNotFoundException("Details do not exist with given id: " + detailsId));

        details.setDate(updatedDetails.getDate());
        details.setTotalOrders(updatedDetails.getTotalOrders());
        details.setTotalAmountSold(updatedDetails.getTotalAmountSold());

        ClosingDetails updatedDetailsObj = closingDetailsRepository.save(details);

        return ClosingDetailsMapper.mapToClosingDetailsDto(updatedDetailsObj);
    }



}
