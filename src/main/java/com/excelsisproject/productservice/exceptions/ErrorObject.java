package com.excelsisproject.productservice.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ErrorObject {
    private Integer statusCode;
    private String message;
    private String timestamp;
}
