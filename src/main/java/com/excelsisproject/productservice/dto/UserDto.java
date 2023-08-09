package com.excelsisproject.productservice.dto;

import com.excelsisproject.productservice.entities.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String userEmail;
    private String userPhoneNumber;
    private String login;
    private String token;
    private Set<Roles> roles;

}
