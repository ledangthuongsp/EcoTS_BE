package com.example.EcoTS.Repositories;

import com.example.EcoTS.Enum.RewardItemClaimStatus;
import com.example.EcoTS.Models.Reward.RewardItemClaim;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;
@Hidden
public interface RewardItemClaimRepository extends JpaRepository<RewardItemClaim, Long> {
    List<RewardItemClaim> findByStatusAndCreatedAtBefore(RewardItemClaimStatus status, Timestamp cutoffTime);
    List<RewardItemClaim> findByUserId(Long userId);
    List<RewardItemClaim> findByLocationIdAndStatus(Long locationId, RewardItemClaimStatus status);

}
