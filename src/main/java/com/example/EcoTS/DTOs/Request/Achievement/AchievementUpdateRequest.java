package com.example.EcoTS.DTOs.Request.Achievement;

import com.example.EcoTS.Enum.AchievementType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AchievementUpdateRequest {
    private Long userId;
    private AchievementType achievementType;
    private int progress;
}
