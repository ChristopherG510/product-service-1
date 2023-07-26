package com.excelsisproject.productservice.dto;

import java.util.Set;

public record SignUpDto (String firstName, String lastName, String login, char[] password){
}
