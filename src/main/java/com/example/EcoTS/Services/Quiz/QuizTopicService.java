package com.example.EcoTS.Services.Quiz;

import com.example.EcoTS.DTOs.Request.Quiz.QuizQuestionDTO;
import com.example.EcoTS.DTOs.Request.Quiz.QuizTopicDTO;
import com.example.EcoTS.Models.QuizQuestion;
import com.example.EcoTS.Models.QuizTopic;
import com.example.EcoTS.Repositories.QuizQuestionRepository;
import com.example.EcoTS.Repositories.QuizTopicRepository;
import com.example.EcoTS.Services.CloudinaryService.CloudinaryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizTopicService {
    @Autowired
    private QuizTopicRepository quizTopicRepository;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    public QuizTopic addTopic(String topicName, String description, MultipartFile file) throws IOException {
        String imgUrl = cloudinaryService.uploadFileQuizTopic(file);
        QuizTopic topic = new QuizTopic();
        topic.setTopicName(topicName);
        topic.setDescription(description);
        topic.setProgress(0.0);
        topic.setImgUrl(imgUrl);
        return quizTopicRepository.save(topic);
    }

    public QuizTopic getTopicById(Long id) {
        return quizTopicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));
    }

    public QuizTopic updateProgress(Long topicId) {
        QuizTopic topic = getTopicById(topicId);
        List<QuizQuestion> questions = quizQuestionRepository.findByQuizTopic(topic);
        int totalQuestions = questions.size();
        int correctAnswers = calculateCorrectAnswers(topic);
        topic.setProgress((correctAnswers / (double) totalQuestions) * 100);
        return quizTopicRepository.save(topic);
    }
    private int calculateCorrectAnswers(QuizTopic topic) {
        // Implement logic to calculate correct answers
        return 0; // Placeholder
    }

}
