package com.example.EcoTS.Services.RewardItem;

import com.example.EcoTS.Models.Reward.RewardItem;
import com.example.EcoTS.Repositories.RewardItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RewardItemService {
    @Autowired
    private RewardItemRepository rewardItemRepository;

    public List<RewardItem> getAllRewards() {
        return rewardItemRepository.findAll();
    }

    public Optional<RewardItem> getRewardById(Long id) {
        return rewardItemRepository.findById(id);
    }

    public RewardItem createReward(RewardItem rewardItem) {
        return rewardItemRepository.save(rewardItem);
    }

    public RewardItem updateReward(Long id, RewardItem updatedReward) {
        return rewardItemRepository.findById(id)
                .map(reward -> {
                    reward.setRewardItemUrl(updatedReward.getRewardItemUrl());
                    reward.setPointCharge(updatedReward.getPointCharge());
                    reward.setStock(updatedReward.getStock());
                    reward.setItemName(updatedReward.getItemName());
                    reward.setItemDescription(updatedReward.getItemDescription());
                    reward.setItemType(updatedReward.getItemType());
                    reward.setHeight(updatedReward.getHeight());
                    reward.setHumidity(updatedReward.getHumidity());
                    reward.setSize(updatedReward.getSize());
                    reward.setWeight(updatedReward.getWeight());
                    return rewardItemRepository.save(reward);
                })
                .orElseThrow(() -> new RuntimeException("Reward not found with id " + id));
    }

    public void deleteReward(Long id) {
        rewardItemRepository.deleteById(id);
    }
}
