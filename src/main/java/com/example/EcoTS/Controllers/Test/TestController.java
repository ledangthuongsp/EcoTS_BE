package com.example.EcoTS.Controllers.Test;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name ="Test")
public class TestController {
    @GetMapping("/test")
    public ResponseEntity<String> testController()
    {
        return ResponseEntity.status(HttpStatus.OK).body("Test completed !");

    }
}
