package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.Achievement;
import com.example.EcoTS.Models.AchievementLevel;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Hidden
public interface AchievementLevelRepository extends JpaRepository<AchievementLevel, Long> {
    List<AchievementLevel> findByAchievementId(Long achievementId);
}
