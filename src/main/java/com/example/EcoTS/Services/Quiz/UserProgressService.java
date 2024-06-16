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
        userProgress.setReachMax(false);
        userProgress.setCollection(true);
        return  userProgressRepository.save(userProgress);
    }

    public Optional<UserProgress> getUserProgress(Long userId, Long topicId) {
        return userProgressRepository.findByUserIdAndTopicId(userId, topicId);
    }
    public UserProgress updateProgress(Long userId, Long topicId, double progress) {
        Optional<UserProgress> optionalUserProgress = userProgressRepository.findByUserIdAndTopicId(userId, topicId);
        UserProgress userProgress = optionalUserProgress.orElseThrow();

        userProgress.setProgress(progress);

        if (progress >= 100 && userProgress.isCollection()) {
            userProgress.setReachMax(true);
            userProgress.setCollection(false);
        }
        return userProgressRepository.save(userProgress);
    }
    public void deleteUserProgress(Long id) {
        userProgressRepository.deleteById(id);
    }
}