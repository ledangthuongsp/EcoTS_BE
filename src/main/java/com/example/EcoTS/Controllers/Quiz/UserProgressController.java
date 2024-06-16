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
                .orElse(userProgressService.creatNew(userId, topicId, 0.0));
        return ResponseEntity.ok(userProgress);
    }
}
