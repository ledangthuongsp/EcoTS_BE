package com.example.EcoTS.Controllers.Achievement;

import com.example.EcoTS.DTOs.Request.Achievement.AchievementAllProgressDTO;
import com.example.EcoTS.DTOs.Request.Achievement.AchievementProgressDTO;
import com.example.EcoTS.Enum.AchievementType;
import com.example.EcoTS.Models.Results;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.ResultRepository;
import com.example.EcoTS.Repositories.UserRepository;
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
    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/result/init")
    public void initializeResultsForAllUsers() {
        resultService.createResultsForAllUsers();
    }
    @GetMapping("/result/get-achievement-progress")
    public ResponseEntity<AchievementProgressDTO> getAchievementProgress(@RequestParam Long userId, @RequestParam Long achievementLevelId) {
        AchievementProgressDTO progress = resultService.getAchievementProgress(userId, achievementLevelId);
        return ResponseEntity.ok().body(progress);
    }
    @GetMapping("/result/get-all-achievement-progress")
    public ResponseEntity<List<AchievementAllProgressDTO>> getAllAchievementProgress(@RequestParam Long userId) {
        List<AchievementAllProgressDTO> progressList = resultService.getAllAchievementProgress(userId);
        return ResponseEntity.ok().body(progressList);
    }
    @PutMapping("/add-maximum-point")
    public void addMaximunPoint(@RequestParam double points, @RequestParam Long userId)
    {
        Users users = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Results results = resultRepository.findByUser(users)
                .orElseThrow(() -> new RuntimeException("Results not found"));
        results.setMaximumPoints(results.getMaximumPoints()+ points);
        resultRepository.save(results);
    }
    @PutMapping("/add-point-donate-total")
    public void addPointDonation(@RequestParam double points, @RequestParam Long userId)
    {
        Users users = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Results results = resultRepository.findByUser(users)
                .orElseThrow(() -> new RuntimeException("Results not found"));
        results.setPointDonate(results.getPointDonate()+ points);
        resultRepository.save(results);
    }
    @PutMapping("/add-save-co2-points")
    public void addSaveCo2(@RequestParam double points, @RequestParam Long userId)
    {
        Users users = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Results results = resultRepository.findByUser(users)
                .orElseThrow(() -> new RuntimeException("Results not found"));
        results.setSaveCo2(results.getSaveCo2()+ points);
        resultRepository.save(results);
    }
    @PutMapping("/add-number-time-of-donate")
    public void addNumberTimeOfDonate(@RequestParam Long time, @RequestParam Long userId)
    {
        Users users = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Results results = resultRepository.findByUser(users)
                .orElseThrow(() -> new RuntimeException("Results not found"));
        results.setNumberOfTimeDonate((int) (results.getNumberOfTimeDonate() + time));
        resultRepository.save(results);
    }
    @PutMapping("/add-number-time-of-detect")
    public void addNumberTimeOfDetect(@RequestParam Long time, @RequestParam Long userId)
    {
        Users users = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Results results = resultRepository.findByUser(users)
                .orElseThrow(() -> new RuntimeException("Results not found"));
        results.setNumberOfTimeDetect((int) (results.getNumberOfTimeDetect() + time));
        resultRepository.save(results);
    }
}
