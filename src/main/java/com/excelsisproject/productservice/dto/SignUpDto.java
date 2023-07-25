package com.excelsisproject.productservice.dto;

import com.excelsisproject.productservice.entities.UserInfo;

public record SignUpDto (String firstName, String lastName, UserInfo userInfo, String login, char[] password){
}
