package com.example.EcoTS.Services.Achievement;

import com.example.EcoTS.Component.AchievementStrategy.AchievementContext;
import com.example.EcoTS.Component.AchievementStrategy.AchievementStrategy;
import com.example.EcoTS.Component.AchievementStrategy.Implements.*;
import com.example.EcoTS.Enum.AchievementType;
import com.example.EcoTS.Models.Achievement;
import com.example.EcoTS.Models.UserAchievement;
import com.example.EcoTS.Repositories.AchievementRepository;
import com.example.EcoTS.Repositories.UserAchievementRepository;
import com.example.EcoTS.Services.CloudinaryService.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AchievementService {

    @Autowired
    private AchievementRepository achievementRepository;
    @Autowired
    private UserAchievementRepository userAchievementRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

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
                throw new RuntimeException("No strategy found f or type: " + type);
            }
        }
    }

    public List<Achievement> getAllAchievements() {
        return achievementRepository.findAll();
    }
    public Achievement createAchievement(String name, String description, long maxIndex, AchievementType type, MultipartFile imageFile, MultipartFile iconFile) throws IOException {
        String imageUrl = cloudinaryService.uploadFileAchievement(imageFile);
        String iconUrl = cloudinaryService.uploadFileLogoAchievement(iconFile);

        Achievement achievement = Achievement.builder()
                .name(name)
                .description(description)
                .imageUrl(imageUrl)
                .iconUrl(iconUrl)
                .maxIndex(maxIndex)
                .type(type)
                .build();

        achievement = achievementRepository.save(achievement);

        return achievement;
    }
}
