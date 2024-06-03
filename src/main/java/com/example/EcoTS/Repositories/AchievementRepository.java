package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.Achievement;
import com.example.EcoTS.Models.UserAchievement;
import com.example.EcoTS.Models.Users;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Hidden
public interface AchievementRepository extends JpaRepository<Achievement, Long> {

}
