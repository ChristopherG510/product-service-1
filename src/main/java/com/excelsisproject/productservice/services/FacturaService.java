package com.excelsisproject.productservice.services;

import com.excelsisproject.productservice.dto.FacturaDto;
import com.excelsisproject.productservice.dto.OrderDto;

import java.util.List;

public interface FacturaService {
    FacturaDto facturar(FacturaDto facturaDto, Long orderId);

    List<FacturaDto> getMisFacturas();

    List<FacturaDto> getAllFacturas();
}
