package com.example.EcoTS.Controllers.Quiz;

import com.example.EcoTS.DTOs.Request.Quiz.QuizTopicDTO;
import com.example.EcoTS.Models.QuizQuestion;
import com.example.EcoTS.Models.QuizTopic;
import com.example.EcoTS.Repositories.QuizTopicRepository;
import com.example.EcoTS.Services.CloudinaryService.CloudinaryService;
import com.example.EcoTS.Services.Quiz.QuizTopicService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/quiz-topics")
@CrossOrigin
@Tag(name = "Quiz")
public class QuizTopicController {
    @Autowired
    private QuizTopicService quizTopicService;
    @Autowired
    private QuizTopicRepository quizTopicRepository;

    @GetMapping("/get-all")
    public List<QuizTopic> getAllTopics() {
        return quizTopicRepository.findAll();
    }

    @GetMapping("/{id}")
    public QuizTopic getTopicById(@PathVariable Long id) {
        return quizTopicRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Topic not found"));
    }

    @PostMapping(value = "/add-new", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public QuizTopic addTopic(@RequestParam String topicName, @RequestParam String description, @RequestPart MultipartFile multipartFile) throws IOException {
        // Gọi service để thêm chủ đề
        return quizTopicService.addTopic(topicName, description, multipartFile);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTopic(@PathVariable Long id) {
        quizTopicService.deleteTopic(id);
        return ResponseEntity.ok("Topic and related data deleted successfully");
    }
    @PutMapping(value = "/update/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public QuizTopic updateTopic(@PathVariable Long id, @RequestParam String topicName, @RequestParam String description, @RequestPart (required = false) MultipartFile multipartFile) throws IOException {
        return quizTopicService.updateTopic(id, topicName, description, multipartFile);
    }
}

