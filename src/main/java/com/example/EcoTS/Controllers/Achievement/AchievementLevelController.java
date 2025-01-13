package com.example.EcoTS.Controllers.Achievement;

import com.example.EcoTS.Enum.AchievementType;
import com.example.EcoTS.Models.Achievement;
import com.example.EcoTS.Models.AchievementLevel;
import com.example.EcoTS.Repositories.AchievementLevelRepository;
import com.example.EcoTS.Repositories.AchievementRepository;
import com.example.EcoTS.Services.Achievement.AchievementLevelService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/achievement")
@Tag(name = "Achievement")
public class AchievementLevelController {
    @Autowired
    private AchievementLevelService achievementLevelService;
    @Autowired
    private AchievementRepository achievementRepository;
    @Autowired
    private AchievementLevelRepository achievementLevelRepository;

    @GetMapping("/get-all-achievement-level")
    public ResponseEntity<List<AchievementLevel>> getAll()
    {
        return ResponseEntity.ok().body(achievementLevelRepository.findAll());
    }
    @GetMapping("/get-achievement-level-by-id")
    public ResponseEntity<AchievementLevel> getAchievementLevelById(@RequestParam Long id)
    {
        return ResponseEntity.ok().body(achievementLevelRepository.findById(id).orElseThrow(() -> new RuntimeException("Achievement level not found")));
    }
    @GetMapping("/get-achievement-level-by-achievement-id")
    public ResponseEntity<List<AchievementLevel>> getAchievementLevelByAchievementId(@RequestParam Long achievementId)
    {
        Achievement achievement = achievementRepository.findById(achievementId).orElseThrow(() -> new RuntimeException("Achievement not found"));
        return ResponseEntity.ok().body(achievementLevelRepository.findByAchievement(achievement));
    }
    @PostMapping(value = "/add-new-achievement-level", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> addNewAchievementLevel(@RequestParam String name, @RequestParam String description,
                                                   @RequestParam Long maxIndex, @RequestParam AchievementType achievementType,
                                                   @RequestPart MultipartFile imgUrl, @RequestPart MultipartFile iconUrl) throws IOException {
        Achievement achievement = achievementRepository.findByType(achievementType);
        return ResponseEntity.ok().body(achievementLevelService.createAchievementLevel(achievement.getId(),name, description,imgUrl,iconUrl,maxIndex));
    }
    @PutMapping("/update-achievement-level")
    public ResponseEntity<?> updateAchievementLevel(@RequestParam String name, @RequestParam String description,
                                                    @RequestParam Long maxIndex, @RequestParam Long achievementLevelId,
                                                    @RequestPart MultipartFile imgUrl, @RequestPart MultipartFile iconUrl) throws IOException {
        return ResponseEntity.ok().body(achievementLevelService.updateAchievementLevel(achievementLevelId, name, description,imgUrl,iconUrl,maxIndex));
    }
    @DeleteMapping("/delete-achievement-level")
    public ResponseEntity<?> deleteAchievementLevel(@RequestParam Long achievementLevelId) {
        achievementLevelService.deleteAchievementLevel(achievementLevelId);
        return ResponseEntity.ok().body("Achievement level deleted successfully.");
    }
}
