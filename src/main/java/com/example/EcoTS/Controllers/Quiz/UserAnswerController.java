package com.example.EcoTS.Controllers.Quiz;

import com.example.EcoTS.Models.UserAnswer;
import com.example.EcoTS.Services.Quiz.UserAnswerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-answer")
@CrossOrigin
@Tag(name = "Quiz")
public class UserAnswerController {
    @Autowired
    private UserAnswerService userAnswerService;

    @PostMapping("/submit")
    public ResponseEntity<UserAnswer> submitAnswer(@RequestBody UserAnswer userAnswer) {
        UserAnswer savedAnswer = userAnswerService.saveUserAnswer(userAnswer);
        return ResponseEntity.ok(savedAnswer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserAnswer(@PathVariable Long id) {
        userAnswerService.deleteUserAnswer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}/topic/{topicId}")
    public ResponseEntity<List<UserAnswer>> getUserAnswersByTopic(@PathVariable Long userId, @PathVariable Long topicId) {
        List<UserAnswer> userAnswers = userAnswerService.getUserAnswersByTopic(userId, topicId);
        return ResponseEntity.ok(userAnswers);
    }
}
