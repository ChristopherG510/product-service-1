package com.excelsisproject.productservice.controllers;

import com.excelsisproject.productservice.dto.ClosingDetailsDto;
import com.excelsisproject.productservice.dto.OrderDto;
import com.excelsisproject.productservice.repositories.ClosingDetailsRepository;
import com.excelsisproject.productservice.services.ClosingService;
import com.excelsisproject.productservice.services.OrderService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/closingDetails")
public class ClosingDetailsController {
    private ClosingService closingService;
    private ClosingDetailsRepository closingDetailsRepository;
    private OrderService orderService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/new")
    public ResponseEntity<ClosingDetailsDto> createDetails(@RequestBody ClosingDetailsDto closingDetailsDto){
        ClosingDetailsDto savedDetails = closingService.createDetails(closingDetailsDto);
        return new ResponseEntity<>(savedDetails, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/view/detailId/{id}")
    public ResponseEntity<ClosingDetailsDto> getDetailsById(@PathVariable("id") Long detailsId){
        ClosingDetailsDto detailsDto = closingService.getDetailsById(detailsId);
        return ResponseEntity.ok(detailsDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/view/all")
    public ResponseEntity<List<ClosingDetailsDto>> getAllDetails(){
        List<ClosingDetailsDto> details = closingService.getAllDetails();
        return ResponseEntity.ok(details);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/edit/detailId/{id}")
    public ResponseEntity<ClosingDetailsDto> updateDetails(@PathVariable("id") Long detailsId, @RequestBody ClosingDetailsDto updatedDetails){
        ClosingDetailsDto detailsDto = closingService.updateDetails(detailsId, updatedDetails);
        return ResponseEntity.ok(detailsDto);
    }

}
