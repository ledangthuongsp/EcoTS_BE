package com.example.EcoTS.Controllers.Test;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(value = "*")
public class TestController {

    @GetMapping("/test")
    public String welcome() {
        return "WELCOME MY API BACKEND SERVER";
    }
}
