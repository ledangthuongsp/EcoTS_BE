package com.example.EcoTS.Controllers.Achievement;

import com.example.EcoTS.Services.Achievement.ResultService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/achievement")
@Tag(name = "Achievement")
public class ResultController {
    @Autowired
    private ResultService resultService;

    @PostMapping("/result/init")
    public void initializeResultsForAllUsers() {
        resultService.createResultsForAllUsers();
    }
}
