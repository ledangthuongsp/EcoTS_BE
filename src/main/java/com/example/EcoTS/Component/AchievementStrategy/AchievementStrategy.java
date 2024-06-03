package com.example.EcoTS.Component.AchievementStrategy;

import com.example.EcoTS.Models.UserAchievement;

public interface AchievementStrategy {
    void process(UserAchievement userAchievement, int increment);
}
