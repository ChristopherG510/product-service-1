package com.excelsisproject.productservice.mappers;

import com.excelsisproject.productservice.dto.FacturaDto;
import com.excelsisproject.productservice.entities.Factura;

public class FacturaMapper {

    public static FacturaDto mapToFacturaDto(Factura factura){
        return new FacturaDto(
                factura.getId(),
                factura.getUserId(),
                factura.getNombre(),
                factura.getRuc(),
                factura.getOrder()
        );
    }

    public static Factura mapToFactura(FacturaDto facturaDto){
        return new Factura(
                facturaDto.getId(),
                facturaDto.getUserId(),
                facturaDto.getNombre(),
                facturaDto.getNombre(),
                facturaDto.getOrder()
        );
    }
}
