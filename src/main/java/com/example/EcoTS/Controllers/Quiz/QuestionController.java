package com.example.EcoTS.Controllers.Quiz;

import com.example.EcoTS.Models.Question;
import com.example.EcoTS.Models.Topic;
import com.example.EcoTS.Services.Quiz.QuestionService;
import com.example.EcoTS.Services.Quiz.TopicService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@Tag(name = "Quiz")
@RequestMapping("/api/questions")
public class QuestionController {
    @Autowired
    QuestionService questionService;

    @GetMapping("/topic/{topicId}")
    public List<Question> getQuestionsByTopic(@PathVariable Long topicId) {
        return questionService.getQuestionsByTopic(topicId);
    }

    @PostMapping
    public Question createQuestion(@RequestBody Question question) {
        return questionService.createQuestion(question);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Question> updateQuestion(@PathVariable Long id, @RequestBody Question questionDetails) {
        Question question = questionService.getQuestionsByTopic(id).stream()
                .filter(q -> q.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (question != null) {
            question.setQuestionText(questionDetails.getQuestionText());
            question.setCorrectAnswer(questionDetails.getCorrectAnswer());
            question.setOptions(questionDetails.getOptions());
            return ResponseEntity.ok(questionService.updateQuestion(question));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }
}
