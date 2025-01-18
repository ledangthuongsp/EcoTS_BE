package com.example.EcoTS.Services.RewardItem;

import com.example.EcoTS.Models.Points;
import com.example.EcoTS.Models.Rank;
import com.example.EcoTS.Models.Reward.RewardHistory;
import com.example.EcoTS.Models.Reward.RewardItem;
import com.example.EcoTS.Models.UserRank;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RewardItemService {
    @Autowired
    private RewardItemRepository rewardItemRepository;
    @Autowired
    private PointRepository pointRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRankRepository userRankRepository;
    @Autowired
    private RewardHistoryRepository rewardHistoryRepository;

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
    @Transactional
    public void updateHistoryCharge(Long userId, double point, Long rewardItemId, Long numberOfItem, Long locationId) {
        // Lấy thông tin người dùng
        Users fetchUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Lấy thông tin điểm người dùng
        Points points = pointRepository.findByUser(fetchUser)
                .orElseThrow(() -> new RuntimeException("Point not found"));

        // Kiểm tra số điểm đủ để đổi
        if (points.getPoint() - point < 0) {
            throw new IllegalArgumentException("Người dùng không đủ điểm để đổi quà.");
        }

        // Trừ điểm
        points.setPoint(points.getPoint() - point);

        // Lấy thông tin phần thưởng
        RewardItem rewardItem = rewardItemRepository.findById(rewardItemId)
                .orElseThrow(() -> new RuntimeException("Reward Item not found"));

        // Kiểm tra số lượng hàng tồn kho
        if (rewardItem.getStock() < numberOfItem) {
            throw new IllegalArgumentException("Không đủ số lượng hàng để đổi quà.");
        }

        // Trừ số lượng trong kho
        rewardItem.setStock((int) (rewardItem.getStock() - numberOfItem));

        // Cập nhật điểm xếp hạng người dùng
        UserRank userRank = userRankRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User Rank not found"));
        userRank.setUserRankPoint(userRank.getUserRankPoint() + point);

        // Lưu lịch sử đổi thưởng
        RewardHistory rewardHistory = new RewardHistory();
        rewardHistory.setRewardItemId(rewardItemId);
        rewardHistory.setUserId(userId);
        rewardHistory.setLocationId(locationId);
        rewardHistoryRepository.save(rewardHistory);
    }

    @Transactional
    public List<RewardHistory> getAllRewardHistoryById(Long userId)
    {
        return rewardHistoryRepository.findByUserId(userId);
    }
}
