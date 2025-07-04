package com.example.EcoTS.Services.RewardItem;

import com.example.EcoTS.Models.*;
import com.example.EcoTS.Models.Reward.RewardHistory;
import com.example.EcoTS.Models.Reward.RewardItem;
import com.example.EcoTS.Repositories.*;
import com.example.EcoTS.Services.CloudinaryService.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private RewardItemLocationRepository rewardItemLocationRepository;

    public List<RewardItem> getAllRewards() {
        return rewardItemRepository.findAll();
    }

    public Optional<RewardItem> getRewardById(Long id) {
        return rewardItemRepository.findById(id);
    }

    public RewardItem createReward(RewardItem rewardItem) {
        return rewardItemRepository.save(rewardItem);
    }

    public RewardItem updateReward(Long id, RewardItem updatedReward, List<MultipartFile> newFiles) {
        return rewardItemRepository.findById(id)
                .map(reward -> {
                    List<String> existingUrls = updatedReward.getRewardItemUrl(); // ảnh cũ từ frontend
                    List<String> newUploadedUrls = new ArrayList<>();

                    if (newFiles != null && !newFiles.isEmpty()) {
                        try {
                            newUploadedUrls = cloudinaryService.updateMultipleRewardImage(newFiles);
                        } catch (IOException e) {
                            throw new RuntimeException("Upload to Cloudinary failed", e);
                        }
                    }

                    // Gộp cả ảnh cũ và ảnh mới
                    List<String> finalUrls = new ArrayList<>();
                    if (existingUrls != null) finalUrls.addAll(existingUrls);
                    finalUrls.addAll(newUploadedUrls);

                    reward.setRewardItemUrl(finalUrls);
                    reward.setPointCharge(updatedReward.getPointCharge());
                    reward.setItemName(updatedReward.getItemName());
                    reward.setItemDescription(updatedReward.getItemDescription());
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
