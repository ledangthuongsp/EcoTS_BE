package com.example.EcoTS.Controllers.Achievement;

import com.example.EcoTS.Models.UserAchievement;
import com.example.EcoTS.Services.Achievement.UserAchievementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/achievement")
@Tag(name ="User Achievement")
public class UserAchievementController {
    @Autowired
    private UserAchievementService userAchievementService;
    @PostMapping("/add-achievement")
    public ResponseEntity<?> addAchievement(@RequestParam Long userId)
    {
        userAchievementService.createInitialUserAchievements(userId);
        return ResponseEntity.ok().body("Tao thanh cong");
    }
}
