package com.example.EcoTS.Component.AchievementStrategy;

import com.example.EcoTS.Component.AchievementStrategy.Implements.*;
import com.example.EcoTS.Enum.AchievementType;
import com.example.EcoTS.Models.UserAchievement;
import lombok.Getter;
import lombok.Setter;

@Setter
public class AchievementContext {
    private AchievementStrategy strategy;

    public AchievementContext(AchievementType type) {
        switch (type) {
            case COUNT_DONATE:
                this.strategy = new DonateAchievementStrategy();
                break;
            case TOTAL_POINTS_DONATE:
                this.strategy = new ContributionPointsAchievementStrategy();
                break;
            case USER_MAX_POINT:
                this.strategy = new AccumulatedPointsAchievementStrategy();
                break;
            case SAVE_CO2:
                this.strategy = new SaveO2AchievementStrategy();
                break;
            case USE_CAMERA_DETECT:
                this.strategy = new CameraDetectAchievementStrategy();
                break;
        }
    }
    public void executeStrategy(UserAchievement userAchievement, int progress) {
        this.strategy.process(userAchievement, progress);
    }
}
