// src/main/java/com/uy1/mgpcalculator/controller/TestController.java
package com.uy1.mgpcalculator.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    
    @GetMapping("/")
    public String home() {
        return "MGP Calculator UY1 - Backend is running!";
    }
    
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}