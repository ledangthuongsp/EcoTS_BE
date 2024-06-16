package com.example.EcoTS.Services.Quiz;

import com.example.EcoTS.Models.UserProgress;
import com.example.EcoTS.Repositories.UserProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProgressService {

    @Autowired
    private UserProgressRepository userProgressRepository;

    public UserProgress saveUserProgress(UserProgress userProgress) {
        return userProgressRepository.save(userProgress);
    }

    public Optional<UserProgress> getUserProgress(Long userId, Long topicId) {
        return userProgressRepository.findByUserIdAndTopicId(userId, topicId);
    }

    public void deleteUserProgress(Long id) {
        userProgressRepository.deleteById(id);
    }
}