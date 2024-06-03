package com.example.EcoTS.Repositories;

import com.example.EcoTS.Enum.AchievementType;
import com.example.EcoTS.Models.Achievement;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Hidden
public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    Achievement findByType(AchievementType type);
}
