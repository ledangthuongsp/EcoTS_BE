package com.example.EcoTS.Services.Achievement;

import com.example.EcoTS.Models.*;
import com.example.EcoTS.Repositories.*;
import com.example.EcoTS.Services.CloudinaryService.CloudinaryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class AchievementLevelService {
    @Autowired
    private AchievementRepository achievementRepository;
    @Autowired
    private AchievementLevelRepository achievementLevelRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private UserAchievementRepository userAchievementRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    @Transactional
    public AchievementLevel createAchievementLevel(Long achievementId, String name, String description, MultipartFile imgUrl, MultipartFile iconUrl, Long maxIndex) throws IOException {
        String imgUrlCloud = cloudinaryService.uploadFileAchievement(imgUrl);
        String iconUrlCloud = cloudinaryService.uploadFileLogoAchievement(iconUrl);
        Achievement achievement = achievementRepository.findById(achievementId).orElseThrow(() -> new RuntimeException("Achievement not found"));
        AchievementLevel level = new AchievementLevel();
        level.setAchievement(achievement);
        level.setName(name);
        level.setDescription(description);
        level.setImgUrl(imgUrlCloud);
        level.setIconUrl(iconUrlCloud);
        level.setMaxIndex(maxIndex);
        return achievementLevelRepository.save(level);
    }
    @Transactional
    public AchievementLevel updateAchievementLevel(Long achievementLevelId, String name, String description, MultipartFile imgUrl, MultipartFile iconUrl, Long maxIndex) throws IOException {
        String imgUrlCloud = cloudinaryService.uploadFileAchievement(imgUrl);
        String iconUrlCloud = cloudinaryService.uploadFileLogoAchievement(iconUrl);
        AchievementLevel level = achievementLevelRepository.findById(achievementLevelId).orElseThrow(() -> new RuntimeException("Achievement not found"));
        level.setName(name);
        level.setDescription(description);
        level.setImgUrl(imgUrlCloud);
        level.setIconUrl(iconUrlCloud);
        level.setMaxIndex(maxIndex);
        return achievementLevelRepository.save(level);
    }
    @Transactional
    public void deleteAchievementLevel(Long achievementLevelId) {
        AchievementLevel level = achievementLevelRepository.findById(achievementLevelId).orElseThrow(() -> new RuntimeException("Achievement level not found"));
        achievementLevelRepository.delete(level);
    }
}
