package com.example.EcoTS.Services.RewardItem;


import com.example.EcoTS.Models.Locations;
import com.example.EcoTS.Models.Reward.RewardItem;
import com.example.EcoTS.Models.Reward.RewardItemLocation;
import com.example.EcoTS.Repositories.LocationRepository;
import com.example.EcoTS.Repositories.RewardItemLocationRepository;
import com.example.EcoTS.Repositories.RewardItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RewardItemLocationService {

    private final RewardItemLocationRepository rewardItemLocationRepository;
    private final RewardItemRepository rewardItemRepository;
    private final LocationRepository locationsRepository;

    /**
     * Thêm một reward item vào địa điểm cụ thể, nếu đã tồn tại thì cập nhật stock
     */

    @Transactional
    public RewardItemLocation addRewardToLocation(Long rewardItemId, Long locationId, Long stock) {
        RewardItem rewardItem = rewardItemRepository.findById(rewardItemId)
                .orElseThrow(() -> new RuntimeException("Reward Item not found"));

        Locations location = locationsRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));

        return rewardItemLocationRepository
                .findByRewardItemAndLocation(rewardItem, location)
                .map(existing -> {
                    existing.setStock(existing.getStock() + stock);
                    return rewardItemLocationRepository.save(existing);
                })
                .orElseGet(() -> rewardItemLocationRepository.save(
                        RewardItemLocation.builder()
                                .rewardItem(rewardItem)
                                .location(location)
                                .stock(stock)
                                .pending(0L)
                                .build()
                ));
    }

    /**
     * Cập nhật lại stock tại một location cho một reward cụ thể
     */
    @Transactional
    public RewardItemLocation updateStock(Long rewardItemId, Long locationId, Long stock) {
        RewardItem rewardItem = rewardItemRepository.findById(rewardItemId)
                .orElseThrow(() -> new RuntimeException("Reward Item not found"));

        Locations location = locationsRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));

        RewardItemLocation itemLocation = rewardItemLocationRepository
                .findByRewardItemAndLocation(rewardItem, location)
                .orElseThrow(() -> new RuntimeException("Reward Item not found at location"));

        itemLocation.setStock(stock);
        return rewardItemLocationRepository.save(itemLocation);
    }

    /**
     * Lấy danh sách item tại 1 địa điểm
     */
    public List<RewardItemLocation> getByLocation(Long locationId) {
        return rewardItemLocationRepository.findByLocationId(locationId);
    }

    /**
     * Tìm 1 reward item location theo reward + location
     */
    public RewardItemLocation getByRewardAndLocation(Long rewardItemId, Long locationId) {
        RewardItem rewardItem = rewardItemRepository.findById(rewardItemId)
                .orElseThrow(() -> new RuntimeException("Reward Item not found"));

        Locations location = locationsRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));

        return rewardItemLocationRepository
                .findByRewardItemAndLocation(rewardItem, location)
                .orElseThrow(() -> new RuntimeException("Reward item not found at location"));
    }
}
