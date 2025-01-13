package com.example.EcoTS.Services.Quiz;

import com.example.EcoTS.Models.UserProgress;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.UserProgressRepository;
import com.example.EcoTS.Repositories.UserRepository;
import com.example.EcoTS.Services.PointService.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserProgressService {

    @Autowired
    private UserProgressRepository userProgressRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PointService pointService;  // Service để xử lý điểm số

    public UserProgress creatNew (Long userId, Long topicId, double progress)
    {
        Users users = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        UserProgress userProgress= new UserProgress();
        userProgress.setProgress(progress);
        userProgress.setUser(users);
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
        UserProgress userProgress = optionalUserProgress.orElseThrow(() -> new IllegalArgumentException("User progress not found"));

        userProgress.setProgress(progress);

        if (progress >= 100) {
            userProgress.setReachMax(true);
            if (userProgress.isCollection()) {
                userProgress.setCollection(false);
                pointService.addPoints(userId, 25); // Cộng điểm khi đạt 100% và collection là true
            }
        }
        return userProgressRepository.save(userProgress);
    }
    @Transactional
    public void resetProgressForTopic(Long topicId) {
        List<UserProgress> userProgressList = userProgressRepository.findByTopicId(topicId);
        for (UserProgress userProgress : userProgressList) {
            userProgress.setProgress(0);
            userProgress.setReachMax(false);
            userProgress.setCollection(true);
            userProgressRepository.save(userProgress);
        }
    }
    public void deleteUserProgress(Long id) {
        userProgressRepository.deleteById(id);
    }
}