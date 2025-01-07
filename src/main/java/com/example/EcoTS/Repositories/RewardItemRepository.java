package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.Reward.RewardItem;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Hidden
public interface RewardItemRepository extends JpaRepository<RewardItem, Long> {
    Optional<RewardItem> findById(Long id);
}
