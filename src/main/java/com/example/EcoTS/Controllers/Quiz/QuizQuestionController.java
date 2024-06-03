package com.example.EcoTS.Controllers.Quiz;

import com.example.EcoTS.Models.QuizQuestion;
import com.example.EcoTS.Services.Quiz.QuizQuestionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz-questions")
@CrossOrigin
@Tag(name = "Quiz")
public class QuizQuestionController {
    @Autowired
    QuizQuestionService quizQuestionService;

    @GetMapping("/topic/{topicId}")
    public List<QuizQuestion> getQuizQuestionsByTopic(@PathVariable Long topicId) {
        return quizQuestionService.getQuizQuestionsByTopic(topicId);
    }

    @PostMapping
    public QuizQuestion createQuizQuestion(@RequestBody QuizQuestion quizQuestion) {
        return quizQuestionService.createQuizQuestion(quizQuestion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuizQuestion> updateQuizQuestion(@PathVariable Long id, @RequestBody QuizQuestion quizQuestionDetails) {
        QuizQuestion quizQuestion = quizQuestionService.getQuizQuestionsByTopic(id).stream()
                .filter(q -> q.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (quizQuestion != null) {
            quizQuestion.setQuestionText(quizQuestionDetails.getQuestionText());
            quizQuestion.setCorrectAnswer(quizQuestionDetails.getCorrectAnswer());
            quizQuestion.setOptions(quizQuestionDetails.getOptions());
            return ResponseEntity.ok(quizQuestionService.updateQuizQuestion(quizQuestion));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuizQuestion(@PathVariable Long id) {
        quizQuestionService.deleteQuizQuestion(id);
        return ResponseEntity.noContent().build();
    }
}

