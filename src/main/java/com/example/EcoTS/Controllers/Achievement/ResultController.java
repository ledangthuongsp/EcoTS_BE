package com.example.EcoTS.Controllers.Achievement;

import com.example.EcoTS.DTOs.Request.Achievement.AchievementProgressDTO;
import com.example.EcoTS.Enum.AchievementType;
import com.example.EcoTS.Services.Achievement.ResultService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/result/get-achievement-progress")
    public ResponseEntity<AchievementProgressDTO> getAchievementProgress(@RequestParam Long userId, @RequestParam Long achievementLevelId) {
        AchievementProgressDTO progress = resultService.getAchievementProgress(userId, achievementLevelId);
        return ResponseEntity.ok().body(progress);
    }
}
