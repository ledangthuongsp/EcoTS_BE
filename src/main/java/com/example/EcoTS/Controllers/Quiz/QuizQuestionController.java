package com.example.EcoTS.Controllers.Quiz;

import com.example.EcoTS.DTOs.Request.Quiz.QuizQuestionDTO;
import com.example.EcoTS.Models.QuizQuestion;
import com.example.EcoTS.Models.QuizTopic;
import com.example.EcoTS.Repositories.QuizQuestionRepository;
import com.example.EcoTS.Repositories.QuizTopicRepository;
import com.example.EcoTS.Services.Quiz.QuizQuestionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/quiz-questions")
@CrossOrigin
@Tag(name = "Quiz")
public class QuizQuestionController {
    @Autowired
    private QuizQuestionService quizQuestionService;
    @Autowired
    private QuizTopicRepository quizTopicRepository;

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;
    @GetMapping("/get-all")
    public List<QuizQuestion> getAllQuestions() {
        return quizQuestionRepository.findAll();
    }

    @GetMapping("/{id}")
    public QuizQuestion getQuestionById(@RequestParam Long id) {
        return quizQuestionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Question not found"));
    }
    @GetMapping("/get-all-question-by-topic")
    public List<QuizQuestion> getAllQuestionByQuizTopic(@RequestParam Long id)
    {
        QuizTopic quizTopic = quizTopicRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Topic not found"));
        return quizQuestionRepository.findByQuizTopic(quizTopic);
    }

    @PostMapping("/add-new-question-to-topic")
    public QuizQuestion saveQuestion(@RequestParam Long topicId ,@RequestBody QuizQuestionDTO quizQuestionDTO) {
        return quizQuestionService.addQuestion(topicId, quizQuestionDTO);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Long id) {
        quizQuestionService.deleteQuestion(id);
        return ResponseEntity.ok("Question deleted successfully");
    }
    @PutMapping("/update/{id}")
    public QuizQuestion updateQuestion(@PathVariable Long id, @RequestBody QuizQuestionDTO quizQuestionDTO) {
        return quizQuestionService.updateQuestion(id, quizQuestionDTO);
    }
}

