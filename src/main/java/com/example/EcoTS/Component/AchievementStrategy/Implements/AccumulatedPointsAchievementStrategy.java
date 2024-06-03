package com.example.EcoTS.Component.AchievementStrategy.Implements;

import com.example.EcoTS.Component.AchievementStrategy.AchievementStrategy;
import com.example.EcoTS.Models.UserAchievement;

public class AccumulatedPointsAchievementStrategy implements AchievementStrategy {

    @Override
    public void process(UserAchievement userAchievement, int progress) {
        // Logic để xử lý theo điểm
        userAchievement.setCurrentProgress(userAchievement.getCurrentProgress() + progress);
        if (userAchievement.getCurrentProgress() >= userAchievement.getAchievement().getMaxIndex()) {
            userAchievement.setAchieved(true);
        }
    }
}
