package com.example.EcoTS.Controllers.Quiz;

import com.example.EcoTS.Models.QuizResult;
import com.example.EcoTS.Services.Quiz.QuizResultService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@Tag(name = "Quiz")
@RequestMapping("/api/quiz-results")
public class QuizResultController {
    @Autowired
    QuizResultService resultService;

    @GetMapping("/user/{userId}/quiz/{quizId}")
    public List<QuizResult> getResultsByUserAndQuiz(@PathVariable Long userId, @PathVariable Long quizId) {
        return resultService.getResultsByUserAndQuiz(userId, quizId);
    }

    @PostMapping
    public QuizResult saveResult(@RequestBody QuizResult result) {
        return resultService.saveResult(result);
    }
}
