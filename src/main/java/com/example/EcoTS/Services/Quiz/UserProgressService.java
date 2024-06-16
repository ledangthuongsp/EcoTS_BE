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

    public UserProgress creatNew (Long userId, Long topicId, double progress)
    {
        UserProgress userProgress= new UserProgress();
        userProgress.setProgress(progress);
        userProgress.setUserId(userId);
        userProgress.setTopicId(topicId);
        return  userProgressRepository.save(userProgress);
    }

    public Optional<UserProgress> getUserProgress(Long userId, Long topicId) {
        return userProgressRepository.findByUserIdAndTopicId(userId, topicId);
    }
    public UserProgress updateProgress(Long userId, Long topicId, double progress) {
        UserProgress userProgress = userProgressRepository.findByUserIdAndTopicId(userId, topicId)
                .orElseThrow();
        userProgress.setProgress(progress);
        return userProgressRepository.save(userProgress);
    }
    public void deleteUserProgress(Long id) {
        userProgressRepository.deleteById(id);
    }
}