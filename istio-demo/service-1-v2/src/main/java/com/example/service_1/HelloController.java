package com.example.service_1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello from Service-1! -V2 ";
    }
    
    @GetMapping("/service-info")
    public String serviceInfo() {
        return "Service-1 - V2 is up and running!";
    }
}