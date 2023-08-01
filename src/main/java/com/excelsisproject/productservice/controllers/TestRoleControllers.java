package com.excelsisproject.productservice.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


// Controller de prueba para los roles ADMIN y CLIENT
@RestController
public class TestRoleControllers {

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/accessAdmin")
    public String accessAdmin(){
        return "Has accedido con rol de administrador";
    }


    @PreAuthorize("hasAuthority('CLIENT')")
    @GetMapping("/accessClient")
    public String accessClient(){
        return "Has accedido con rol de cliente";
    }
}
