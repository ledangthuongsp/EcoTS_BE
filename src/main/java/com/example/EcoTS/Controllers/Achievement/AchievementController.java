package com.example.EcoTS.Controllers.Achievement;

import com.example.EcoTS.Enum.AchievementType;
import com.example.EcoTS.Models.Achievement;
import com.example.EcoTS.Repositories.AchievementRepository;
import com.example.EcoTS.Services.Achievement.AchievementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin
@RequestMapping("/achievement")
@Tag(name = "Achievement")
public class AchievementController {

    @Autowired
    private AchievementService achievementService;
    @Autowired
    private AchievementRepository achievementRepository;

    @PostMapping("/create")
    public ResponseEntity<Achievement> createAchievement(@RequestParam AchievementType type) {
        return ResponseEntity.ok(achievementService.createAchievement(type));
    }
    @GetMapping
    public ResponseEntity<List<Achievement>> getAllAchievement()
    {
        return ResponseEntity.ok(achievementRepository.findAll());
    }
}