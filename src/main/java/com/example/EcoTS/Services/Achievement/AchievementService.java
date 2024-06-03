package com.example.EcoTS.Services.Achievement;

import com.example.EcoTS.Component.AchievementStrategy.AchievementContext;
import com.example.EcoTS.Component.AchievementStrategy.AchievementStrategy;
import com.example.EcoTS.Component.AchievementStrategy.Implements.*;
import com.example.EcoTS.Enum.AchievementType;
import com.example.EcoTS.Models.Achievement;
import com.example.EcoTS.Models.UserAchievement;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.AchievementRepository;
import com.example.EcoTS.Repositories.UserAchievementRepository;
import com.example.EcoTS.Repositories.UserRepository;
import com.example.EcoTS.Services.CloudinaryService.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AchievementService {

    @Autowired
    private AchievementRepository achievementRepository;
    @Autowired
    private UserAchievementRepository userAchievementRepository;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private UserRepository userRepository;

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
        initializeUserAchievementsForAllUsers(achievement);
        return achievement;
    }
    private void initializeUserAchievementsForAllUsers(Achievement achievement) {
        List<Users> allUsers = userRepository.findAll();
        for (Users user : allUsers) {
            UserAchievement userAchievement = new UserAchievement();
            userAchievement.setUserId(user.getId());
            userAchievement.setAchievementId(achievement.getId());
            userAchievement.setCurrentProgress(0);
            userAchievement.setAchieved(false);
            userAchievementRepository.save(userAchievement);
        }
    }
    public void updateAchievementProgress(Long userId, Long achievementId, int progress) {
        UserAchievement userAchievement = userAchievementRepository.findByUserIdAndAchievementId(userId, achievementId);
        Achievement achievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new IllegalArgumentException("Achievement not found."));
        if (userAchievement != null) {
            AchievementContext context = new AchievementContext(achievement.getType());
            context.executeStrategy(userAchievement, progress);
            userAchievementRepository.save(userAchievement);
        } else {
            throw new IllegalStateException("UserAchievement not found for given userId and achievementId");
        }
    }
}
