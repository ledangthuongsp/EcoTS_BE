package com.example.EcoTS.Component.AchievementStrategy;

import com.example.EcoTS.Models.UserAchievement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AchievementContext {
    // Setter để thay đổi Strategy
    private AchievementStrategy strategy;

    // Setter để thay đổi Strategy
    public void setStrategy(AchievementStrategy strategy) {
        this.strategy = strategy;
    }

    // Phương thức để thực hiện process theo Strategy hiện tại
    public void executeStrategy(UserAchievement userAchievement, int increment) {
        this.strategy.process(userAchievement, increment);
    }
}
