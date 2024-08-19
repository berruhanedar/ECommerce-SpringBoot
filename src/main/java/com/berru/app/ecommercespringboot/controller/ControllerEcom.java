package com.berru.app.ecommercespringboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerEcom {

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
