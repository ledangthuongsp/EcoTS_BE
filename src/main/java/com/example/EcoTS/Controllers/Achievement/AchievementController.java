package com.example.EcoTS.Controllers.Achievement;

import com.example.EcoTS.DTOs.Request.Achievement.AchievementUpdateRequest;
import com.example.EcoTS.Enum.AchievementType;
import com.example.EcoTS.Models.Achievement;
import com.example.EcoTS.Services.Achievement.AchievementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/achievement")
@Tag(name ="Achievement")
public class AchievementController {
    @Autowired
    private AchievementService achievementService;


    @GetMapping("/get-all-achievements")
    public List<Achievement> getAllAchievements() {
        return achievementService.getAllAchievements();
    }

    @PostMapping(value = "/create-new-achievement", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<String> createAchievement(
            @RequestParam String achievementName,
            @RequestParam String achievementDescription,
            @RequestParam long maxIndex,
            @RequestParam AchievementType type,
            @RequestPart("imageFile") MultipartFile imageFile,
            @RequestPart("iconFile") MultipartFile iconFile) throws IOException {
        try {
            achievementService.createAchievement(achievementName, achievementDescription, maxIndex, type, imageFile, iconFile);
            return ResponseEntity.ok().body("Create successful");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Error: " + e.getMessage(), e);
        }
    }
    @PostMapping("/update")
    public ResponseEntity<String> updateAchievementProgress(@RequestBody AchievementUpdateRequest request) {
        try {
            achievementService.updateAchievementProgress(request.getUserId(), request.getAchievementType(), request.getProgress());
            return ResponseEntity.ok("Achievement updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating achievement: " + e.getMessage());
        }
    }
}
