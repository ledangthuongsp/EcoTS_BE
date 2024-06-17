package com.example.EcoTS.Controllers.Quiz;

import com.example.EcoTS.Models.UserProgress;
import com.example.EcoTS.Repositories.UserProgressRepository;
import com.example.EcoTS.Services.Quiz.UserProgressService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user-progress")
@CrossOrigin
@Tag(name = "Quiz")
public class UserProgressController {
    @Autowired
    private UserProgressRepository userProgressRepository;
    @Autowired
    private UserProgressService userProgressService;

    @GetMapping("/user/{userId}/topic/{topicId}")
    public ResponseEntity<UserProgress> getUserProgress(@PathVariable Long userId, @PathVariable Long topicId) {
        UserProgress userProgress = userProgressRepository.findByUserIdAndTopicId(userId, topicId)
                .orElseThrow();
        return ResponseEntity.ok(userProgress);
    }
    @PutMapping("/user/{userId}/topic/{topicId}")
    public ResponseEntity<UserProgress> updateUserProgress(@PathVariable Long userId, @PathVariable Long topicId, @RequestParam double progress) {
        UserProgress userProgress = userProgressService.updateProgress(userId, topicId, progress);
        return ResponseEntity.ok(userProgress);
    }
}
