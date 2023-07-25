package com.excelsisproject.productservice.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRoleControllers {

    @GetMapping("/accessAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public String accessAdmin(){
        return "Has accedido con rol de administrador";
    }

    @GetMapping("/accessClient")
    @PreAuthorize("hasRole('CLIENT')")
    public String accessClient(){
        return "Has accedido con rol de cliente";
    }
}
