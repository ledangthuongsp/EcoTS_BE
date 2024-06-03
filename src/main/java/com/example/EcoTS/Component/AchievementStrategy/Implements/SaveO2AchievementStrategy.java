package com.example.EcoTS.Component.AchievementStrategy.Implements;

import com.example.EcoTS.Component.AchievementStrategy.AchievementStrategy;
import com.example.EcoTS.Models.UserAchievement;

public class SaveO2AchievementStrategy implements AchievementStrategy {

    @Override
    public void process(UserAchievement userAchievement, int increment) {
        userAchievement.setCurrentProgress(userAchievement.getCurrentProgress() + increment);
        if (userAchievement.getCurrentProgress() >= userAchievement.getAchievement().getMaxIndex()) {
            userAchievement.setAchieved(true);
        }
    }
}
