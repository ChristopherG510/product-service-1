package com.excelsisproject.productservice.dto;

public record SignUpDto (String firstName, String lastName, String userEmail, String userPhoneNumber, String userAddress,String login, char[] password){
}
