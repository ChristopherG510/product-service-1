package com.excelsisproject.productservice.controller;

import com.excelsisproject.productservice.dto.ClosingDetailsDto;
import com.excelsisproject.productservice.dto.OrderDto;
import com.excelsisproject.productservice.dto.ProductDto;
import com.excelsisproject.productservice.repository.ClosingDetailsRepository;
import com.excelsisproject.productservice.service.ClosingService;
import com.excelsisproject.productservice.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RestController
@RequestMapping("/api/closingDetails")
public class ClosingDetailsController {
    private ClosingService closingService;
    private ClosingDetailsRepository closingDetailsRepository;
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<ClosingDetailsDto> createDetails(@RequestBody ClosingDetailsDto closingDetailsDto){

        String date = closingDetailsDto.getDate();
        List<OrderDto> orderDtoList = orderService.findByDate(date);
        double amountSold = 0;
        double totalOrders = 0;

        for(OrderDto order : orderDtoList){
            amountSold += order.getTotalPrice();
            totalOrders += 1;
        }

        closingDetailsDto.setTotalAmountSold(amountSold);
        closingDetailsDto.setTotalOrders(totalOrders);

        ClosingDetailsDto savedDetails = closingService.createDetails(closingDetailsDto);
        return new ResponseEntity<>(savedDetails, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<ClosingDetailsDto> getDetailstById(@PathVariable("id") Long detailsId){
        ClosingDetailsDto detailsDto = closingService.getDetailsById(detailsId);
        return ResponseEntity.ok(detailsDto);
    }

    @GetMapping
    public ResponseEntity<List<ClosingDetailsDto>> getAllDetails(){
        List<ClosingDetailsDto> details = closingService.getAllDetails();
        return ResponseEntity.ok(details);
    }

    @PutMapping("{id}")
    public ResponseEntity<ClosingDetailsDto> updateDetails(@PathVariable("id") Long detailsId, @RequestBody ClosingDetailsDto updatedDetails){
        ClosingDetailsDto detailsDto = closingService.updateDetails(detailsId, updatedDetails);
        return ResponseEntity.ok(detailsDto);
    }

}
