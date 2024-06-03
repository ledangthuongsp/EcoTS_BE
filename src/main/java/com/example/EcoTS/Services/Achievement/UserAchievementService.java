package com.example.EcoTS.Services.Achievement;

import com.example.EcoTS.Models.Achievement;
import com.example.EcoTS.Models.UserAchievement;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.AchievementRepository;
import com.example.EcoTS.Repositories.UserAchievementRepository;
import com.example.EcoTS.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAchievementService {

    @Autowired
    private UserAchievementRepository userAchievementRepository;
    @Autowired
    private AchievementRepository achievementRepository;
    @Autowired
    private UserRepository userRepository;

    public List<UserAchievement> getUserAchievements(Long userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return userAchievementRepository.findByUser(user);
    }

    public void createInitialUserAchievements(Long userId) {
        Users users = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<Achievement> achievements = achievementRepository.findAll();
        for (Achievement achievement : achievements) {
            UserAchievement userAchievement = UserAchievement.builder()
                    .user(users)
                    .achievement(achievement)
                    .currentProgress(0)
                    .achieved(false)
                    .build();
            userAchievementRepository.save(userAchievement);
        }
    }
}
