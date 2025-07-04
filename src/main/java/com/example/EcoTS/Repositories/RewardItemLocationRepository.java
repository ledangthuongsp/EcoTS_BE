package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.Locations;
import com.example.EcoTS.Models.Reward.RewardItem;
import com.example.EcoTS.Models.Reward.RewardItemLocation;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
@Hidden
public interface RewardItemLocationRepository extends JpaRepository<RewardItemLocation, Long> {
    Optional<RewardItemLocation> findByRewardItemAndLocation(RewardItem rewardItem, Locations location);
    List<RewardItemLocation> findByLocationId(Long locationId);
    List<RewardItemLocation> findByRewardItemId(Long rewardItemId);

}
