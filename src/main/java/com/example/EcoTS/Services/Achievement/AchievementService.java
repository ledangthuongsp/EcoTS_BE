package com.example.EcoTS.Services.Achievement;

import com.example.EcoTS.Component.AchievementStrategy.AchievementContext;
import com.example.EcoTS.Component.AchievementStrategy.AchievementStrategy;
import com.example.EcoTS.Component.AchievementStrategy.Implements.*;
import com.example.EcoTS.Enum.AchievementType;
import com.example.EcoTS.Models.UserAchievement;
import com.example.EcoTS.Repositories.UserAchievementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AchievementService {

    @Autowired
    private UserAchievementRepository userAchievementRepository;

    private Map<AchievementType, AchievementStrategy> strategyMap;

    public AchievementService() {
        strategyMap = new HashMap<>();
        strategyMap.put(AchievementType.COUNT_DONATE, new DonateAchievementStrategy());
        strategyMap.put(AchievementType.SAVE_CO2, new SaveO2AchievementStrategy());
        strategyMap.put(AchievementType.TOTAL_POINTS_DONATE, new ContributionPointsAchievementStrategy());
        strategyMap.put(AchievementType.USER_MAX_POINT, new AccumulatedPointsAchievementStrategy());
        strategyMap.put(AchievementType.USE_CAMERA_DETECT, new CameraDetectAchievementStrategy());
    }

    public void processAchievement(Long userAchievementId, int increment, AchievementType type) {
        Optional<UserAchievement> optionalUserAchievement = userAchievementRepository.findById(userAchievementId);
        if (optionalUserAchievement.isPresent()) {
            UserAchievement userAchievement = optionalUserAchievement.get();
            AchievementContext context = new AchievementContext();
            AchievementStrategy strategy = strategyMap.get(type);
            if (strategy != null) {
                context.setStrategy(strategy);
                context.executeStrategy(userAchievement, increment);
                userAchievementRepository.save(userAchievement);
            } else {
                throw new RuntimeException("No strategy found for type: " + type);
            }
        }
    }
}
