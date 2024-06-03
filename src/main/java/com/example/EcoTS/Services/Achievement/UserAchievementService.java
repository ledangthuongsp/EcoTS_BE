package com.example.EcoTS.Services.Achievement;

import com.example.EcoTS.Models.UserAchievement;
import com.example.EcoTS.Repositories.UserAchievementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAchievementService {
    @Autowired
    private UserAchievementRepository userAchievementRepository;

    public List<UserAchievement> getUserAchievements(Long userId) {
        return userAchievementRepository.findByUserId(userId);
    }
}
