package com.excelsisproject.productservice.services.implementation;

import com.excelsisproject.productservice.dto.FacturaDto;
import com.excelsisproject.productservice.entities.Factura;
import com.excelsisproject.productservice.mappers.FacturaMapper;
import com.excelsisproject.productservice.mappers.OrderMapper;
import com.excelsisproject.productservice.repositories.FacturaRepository;
import com.excelsisproject.productservice.services.FacturaService;
import com.excelsisproject.productservice.services.OrderService;
import com.excelsisproject.productservice.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FacturaServiceImpl implements FacturaService {

    private FacturaRepository facturaRepository;
    private OrderService orderService;
    private UserService userService;

    @Override
    public FacturaDto facturar(FacturaDto facturaDto, Long orderId) {

        facturaDto.setOrder(OrderMapper.mapToOrder(orderService.getOrderById(orderId)));
        Factura factura = FacturaMapper.mapToFactura(facturaDto);
        Factura savedFactura = facturaRepository.save(factura);
        return FacturaMapper.mapToFacturaDto(savedFactura);
    }

    @Override
    public List<FacturaDto> getMisFacturas() {
        List<Factura> facturas = facturaRepository.findAllByUserId(userService.getLoggedUserId());
        return facturas.stream().map(FacturaMapper::mapToFacturaDto).collect(Collectors.toList());
    }

    @Override
    public List<FacturaDto> getAllFacturas() {
        List<Factura> facturas = facturaRepository.findAll();
        return facturas.stream().map(FacturaMapper::mapToFacturaDto).collect(Collectors.toList());
    }


}
