package com.excelsisproject.productservice.dto;

import com.excelsisproject.productservice.entities.UserInfo;

public record SignUpDto (String firstName, String lastName, String userEmail, String userPhoneNumber, String userAddress,String login, char[] password){
}
