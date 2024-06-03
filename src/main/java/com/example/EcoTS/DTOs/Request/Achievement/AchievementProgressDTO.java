package com.example.EcoTS.DTOs.Request.Achievement;

import com.example.EcoTS.Enum.AchievementType;
import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AchievementProgressDTO {
    private AchievementType achievementType;
    private double progress;
}