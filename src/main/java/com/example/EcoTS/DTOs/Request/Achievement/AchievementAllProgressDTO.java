package com.example.EcoTS.DTOs.Request.Achievement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AchievementAllProgressDTO {
    private String achievementLevelName;
    private Long achievementId;
    private double currentIndex;
    private double maxIndex;
    private double progress;
}
