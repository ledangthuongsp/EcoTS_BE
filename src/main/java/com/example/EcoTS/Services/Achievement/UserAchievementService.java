package com.example.EcoTS.Services.Achievement;

import com.example.EcoTS.Enum.AchievementType;
import com.example.EcoTS.Models.AchievementLevel;
import com.example.EcoTS.Models.Results;
import com.example.EcoTS.Models.UserAchievement;
import com.example.EcoTS.Repositories.AchievementLevelRepository;
import com.example.EcoTS.Repositories.ResultRepository;
import com.example.EcoTS.Repositories.UserAchievementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.EcoTS.Enum.AchievementType.*;

@Service
public class UserAchievementService {

    @Autowired
    private UserAchievementRepository userAchievementRepository;
    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private AchievementLevelRepository achievementLevelRepository;
    public void updateBadgeUrlIfAchieved(Long userId, Long achievementLevelId) {
        UserAchievement userAchievement = userAchievementRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Results results = resultRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Results not found"));
        AchievementLevel level = achievementLevelRepository.findById(achievementLevelId)
                .orElseThrow(() -> new RuntimeException("Achievement Level not found"));

        double currentUserIndex = getCurrentUserIndexByType(results, level.getAchievement().getType());

        // Check if the user has reached the required index
        if (currentUserIndex >= level.getMaxIndex()) {
            if (!userAchievement.getBadgeUrl().contains(level.getIconUrl())) {
                userAchievement.getBadgeUrl().add(level.getIconUrl());
                userAchievementRepository.save(userAchievement);
            }
        }
    }
    private double getCurrentUserIndexByType(Results results, AchievementType type) {
        switch (type) {
            case COUNT_DONATE:
                return results.getNumberOfTimeDonate();
            case TOTAL_POINTS_DONATE:
                return results.getPointDonate();
            case USER_MAX_POINT:
                return results.getMaximumPoints();
            case SAVE_CO2:
                return results.getSaveCo2();
            case USE_CAMERA_DETECT:
                return results.getNumberOfTimeDetect();
            default:
                return 0;
        }
    }
}
