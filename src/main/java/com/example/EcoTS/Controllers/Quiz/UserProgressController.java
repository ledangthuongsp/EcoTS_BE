package com.example.EcoTS.Controllers.Quiz;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/user-progress")
@CrossOrigin
@Tag(name = "Quiz")
public class UserProgressController {
}
