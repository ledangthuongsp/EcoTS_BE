package com.example.EcoTS.Component.AchievementStrategy.Implements;

import com.example.EcoTS.Component.AchievementStrategy.AchievementStrategy;
import com.example.EcoTS.Models.Achievement;
import com.example.EcoTS.Models.UserAchievement;
import com.example.EcoTS.Repositories.AchievementRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class DonateAchievementStrategy implements AchievementStrategy {

    @Autowired
    private AchievementRepository achievementRepository;
    @Override
    public void process(UserAchievement userAchievement, int progress) {
        int newProgress = userAchievement.getCurrentProgress() + progress;
        Achievement achievement = achievementRepository.findById(userAchievement.getAchievementId())
                .orElseThrow(() -> new IllegalArgumentException("Achievement not found."));
        if (newProgress >= achievement.getMaxIndex()) {
            userAchievement.setCurrentProgress((int) achievement.getMaxIndex());
            userAchievement.setAchieved(true);
        } else {
            userAchievement.setCurrentProgress(newProgress);
        }
    }
}
