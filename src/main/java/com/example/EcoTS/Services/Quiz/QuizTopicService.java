package com.example.EcoTS.Services.Quiz;

import com.example.EcoTS.DTOs.Request.Quiz.QuizQuestionDTO;
import com.example.EcoTS.DTOs.Request.Quiz.QuizTopicDTO;
import com.example.EcoTS.Enum.Roles;
import com.example.EcoTS.Models.QuizQuestion;
import com.example.EcoTS.Models.QuizTopic;
import com.example.EcoTS.Models.UserProgress;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.QuizQuestionRepository;
import com.example.EcoTS.Repositories.QuizTopicRepository;
import com.example.EcoTS.Repositories.UserProgressRepository;
import com.example.EcoTS.Repositories.UserRepository;
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
    @Autowired
    private UserProgressRepository userProgressRepository;
    @Autowired
    private UserRepository userRepository;
    @Transactional
    public QuizTopic addTopic(String topicName, String description, MultipartFile file, String username) throws IOException {
        Users user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!Roles.EMPLOYEE.name().equals(user.getRole())) {
            throw new IllegalArgumentException("Only employees can add topics");
        }

        String imgUrl = cloudinaryService.uploadFileQuizTopic(file);
        QuizTopic topic = new QuizTopic();
        topic.setTopicName(topicName);
        topic.setDescription(description);
        topic.setImgUrl(imgUrl);
        topic.setNumberQuestion(0L);
        QuizTopic savedTopic = quizTopicRepository.save(topic);

        // Add progress for all users
        List<Users> users = userRepository.findAll();
        for (Users u : users) {
            UserProgress userProgress = new UserProgress();
            userProgress.setTopicId(savedTopic.getId());
            userProgress.setUser(u);
            userProgress.setProgress(0.0);
            userProgress.setCollection(true);
            userProgress.setReachMax(false);
            userProgressRepository.save(userProgress);
        }
        return savedTopic;
    }

    @Transactional
    public QuizTopic getTopicById(Long id) {
        return quizTopicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));
    }
}
