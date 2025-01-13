package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.UserAchievement;
import com.example.EcoTS.Models.UserProgress;
import com.example.EcoTS.Models.Users;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
@Hidden
public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    List<UserProgress> findByUserId(Long userId);
    Optional<UserProgress> findByUserIdAndTopicId(Long userId, Long topicId);
    void deleteByUserId(Long userId);
    Optional<UserProgress> deleteByUser(Users user);
    List<UserProgress> findByTopicId(Long topicId);
}
