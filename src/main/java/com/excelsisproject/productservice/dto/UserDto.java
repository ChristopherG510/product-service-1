package com.excelsisproject.productservice.dto;

import com.excelsisproject.productservice.entities.Roles;
import lombok.*;

import java.util.Set;

@Getter
@Setter
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
    private String ruc;
    private String login;
    private String token;
    private Set<String> roles;

}
