package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    List<UserProgress> findByUserId(Long userId);
    Optional<UserProgress> findByUserIdAndTopicId(Long userId, Long topicId);
}
