package com.excelsisproject.productservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequestDto {

    String column;
    String value;
    Operation operation;

    public enum Operation{
        IGUAL, CONTIENE, EN, MAYOR_QUE, MENOR_QUE, ENTRE, FECHA_ENTRE, FECHA_DESPUES, FECHA_ANTES;
    }
}
