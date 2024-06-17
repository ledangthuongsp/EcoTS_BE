package com.example.EcoTS.Services.Achievement;

import com.example.EcoTS.DTOs.Request.Achievement.AchievementAllProgressDTO;
import com.example.EcoTS.DTOs.Request.Achievement.UserAchievementDTO;
import com.example.EcoTS.Enum.AchievementType;
import com.example.EcoTS.Models.AchievementLevel;
import com.example.EcoTS.Models.Results;
import com.example.EcoTS.Models.UserAchievement;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.AchievementLevelRepository;
import com.example.EcoTS.Repositories.ResultRepository;
import com.example.EcoTS.Repositories.UserAchievementRepository;
import com.example.EcoTS.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.EcoTS.Enum.AchievementType.*;

@Service
public class UserAchievementService {

    @Autowired
    private UserAchievementRepository userAchievementRepository;
    @Autowired
    private UserRepository userRepository;

    public UserAchievement createNew (Long userId, List<String> badgeUrl) {
        // Xuất
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        UserAchievement userAchievement = new UserAchievement();
        userAchievement.setUser(user);
        userAchievement.setBadgeUrl(badgeUrl);
        return userAchievementRepository.save(userAchievement);
    }
}
