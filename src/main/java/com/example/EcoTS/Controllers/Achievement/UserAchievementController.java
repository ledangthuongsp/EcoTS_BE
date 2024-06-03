package com.example.EcoTS.Controllers.Achievement;

import com.example.EcoTS.Services.Achievement.AchievementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/achievement")
@Tag(name ="User Achievement")
public class UserAchievementController {
    @Autowired
    private AchievementService achievementService;

}
