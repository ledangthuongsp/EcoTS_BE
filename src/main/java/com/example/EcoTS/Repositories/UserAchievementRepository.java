package com.example.EcoTS.Repositories;

import com.example.EcoTS.Enum.AchievementType;
import com.example.EcoTS.Models.UserAchievement;
import com.example.EcoTS.Models.Users;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Hidden
public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {
    List<UserAchievement> findByUserId(Long userId);
    List<UserAchievement> findByUserIdAndAchievementType(Long userId, AchievementType type);
}
