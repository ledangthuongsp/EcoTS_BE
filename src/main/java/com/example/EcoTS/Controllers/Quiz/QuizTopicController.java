package com.example.EcoTS.Controllers.Quiz;

import com.example.EcoTS.Models.QuizTopic;
import com.example.EcoTS.Services.Quiz.QuizTopicService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz-topics")
@CrossOrigin
@Tag(name = "Quiz")
public class QuizTopicController {
    @Autowired
    QuizTopicService quizTopicService;

    @GetMapping
    public List<QuizTopic> getAllQuizTopics() {
        return quizTopicService.getAllQuizTopics();
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizTopic> getQuizTopicById(@PathVariable Long id) {
        QuizTopic quizTopic = quizTopicService.getQuizTopicById(id);
        if (quizTopic != null) {
            return ResponseEntity.ok(quizTopic);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public QuizTopic createQuizTopic(@RequestBody QuizTopic quizTopic) {
        return quizTopicService.createQuizTopic(quizTopic);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuizTopic> updateQuizTopic(@PathVariable Long id, @RequestBody QuizTopic quizTopicDetails) {
        QuizTopic quizTopic = quizTopicService.getQuizTopicById(id);
        if (quizTopic != null) {
            quizTopic.setName(quizTopicDetails.getName());
            quizTopic.setImage(quizTopicDetails.getImage());
            quizTopic.setProgress(quizTopicDetails.getProgress());
            return ResponseEntity.ok(quizTopicService.updateQuizTopic(quizTopic));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuizTopic(@PathVariable Long id) {
        quizTopicService.deleteQuizTopic(id);
        return ResponseEntity.noContent().build();
    }
}

