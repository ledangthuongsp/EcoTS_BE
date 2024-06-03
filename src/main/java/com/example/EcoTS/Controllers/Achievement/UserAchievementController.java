package com.example.EcoTS.Controllers.Achievement;

import com.example.EcoTS.DTOs.Request.Achievement.AchievementUpdateRequest;
import com.example.EcoTS.Enum.AchievementType;
import com.example.EcoTS.Models.UserAchievement;
import com.example.EcoTS.Services.Achievement.AchievementService;
import com.example.EcoTS.Services.Achievement.UserAchievementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/achievement")
@Tag(name ="User Achievement")
public class UserAchievementController {
    @Autowired
    private UserAchievementService userAchievementService;
    @Autowired
    private AchievementService achievementService;
    @PostMapping("/add-achievement")
    public ResponseEntity<?> addAchievement(@RequestParam Long userId)
    {
        userAchievementService.createInitialUserAchievements(userId);
        return ResponseEntity.ok().body("Tao thanh cong");
    }
    @GetMapping("/view-achievements")
    public List<UserAchievement> getUserAchievements(@RequestParam Long userId) {
        return userAchievementService.getUserAchievements(userId);
    }

}
