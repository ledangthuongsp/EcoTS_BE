package com.example.EcoTS.Services.Achievement;

import com.example.EcoTS.Enum.AchievementType;
import com.example.EcoTS.Models.Achievement;
import com.example.EcoTS.Repositories.AchievementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AchievementService {
    @Autowired
    private AchievementRepository achievementRepository;

    public Achievement createAchievement(AchievementType type) {
        Achievement achievement = new Achievement();
        achievement.setType(type);
        return achievementRepository.save(achievement);
    }

}
