package com.example.EcoTS.Controllers.Quiz;


import com.example.EcoTS.Models.QuizResult;
import com.example.EcoTS.Models.QuizTopic;
import com.example.EcoTS.Repositories.QuizResultRepository;
import com.example.EcoTS.Services.Quiz.QuizResultService;
import com.example.EcoTS.Services.Quiz.QuizTopicService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@Tag(name = "Quiz")
@RequestMapping("/api/quiz-results")
public class QuizResultController {
    @Autowired
    private QuizResultService quizResultService;
    @Autowired
    private QuizResultRepository quizResultRepository;

    @Autowired
    private QuizTopicService quizTopicService;

    @GetMapping
    public List<QuizResult> getAllResults() {
        return quizResultRepository.findAll();
    }

    @GetMapping("/{id}")
    public QuizResult getResultById(@PathVariable Long id) {
        return quizResultRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Quiz Result not found"));
    }

    @PostMapping("/result")
    public ResponseEntity<QuizResult> saveResult(@RequestParam Long userId, @RequestParam Long topicId,
                                                 @RequestParam int correctAnswers, @RequestParam int totalQuestions) {
        return ResponseEntity.ok(quizResultService.saveResult(userId, topicId, correctAnswers, totalQuestions));
    }

    @PutMapping("/topic/{topicId}/progress")
    public ResponseEntity<QuizTopic> updateProgress(@PathVariable Long topicId) {
        return ResponseEntity.ok(quizTopicService.updateProgress(topicId));
    }
}
