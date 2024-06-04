package com.example.EcoTS.Services.Quiz;

import com.example.EcoTS.Models.QuizResult;
import com.example.EcoTS.Models.QuizTopic;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.QuizResultRepository;
import com.example.EcoTS.Repositories.QuizTopicRepository;
import com.example.EcoTS.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizResultService {
    @Autowired
    private QuizResultRepository quizResultRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizTopicService quizTopicService;

    public QuizResult saveResult(Long userId, Long topicId, int correctAnswers, int totalQuestions) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        QuizTopic topic = quizTopicService.getTopicById(topicId);

        QuizResult result = QuizResult.builder()
                .users(user)
                .quizTopic(topic)
                .correctAnswers(correctAnswers)
                .totalQuestions(totalQuestions)
                .build();

        return quizResultRepository.save(result);
    }
}

