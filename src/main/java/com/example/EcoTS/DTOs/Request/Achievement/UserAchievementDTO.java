package com.example.EcoTS.DTOs.Request.Achievement;

import com.example.EcoTS.Models.Users;
import lombok.Data;

import java.util.List;

@Data
public class UserAchievementDTO {
    private Users users;
    private List<String> badgeUrl;
}
