package com.example.EcoTS.Services.RewardItem;


import com.example.EcoTS.DTOs.Response.RewardItemStockResponse;
import com.example.EcoTS.Models.Locations;
import com.example.EcoTS.Models.Reward.RewardItem;
import com.example.EcoTS.Models.Reward.RewardItemLocation;
import com.example.EcoTS.Repositories.LocationRepository;
import com.example.EcoTS.Repositories.RewardItemLocationRepository;
import com.example.EcoTS.Repositories.RewardItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RewardItemLocationService {

    @Autowired
    private RewardItemLocationRepository rewardItemLocationRepository;
    @Autowired
    private RewardItemRepository rewardItemRepository;
    @Autowired
    private LocationRepository locationRepository;

    @Transactional
    public List<RewardItemStockResponse> getLowStockItemsByLocation(Long locationId, Long threshold) {
        Locations location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));

        List<RewardItemLocation> rilList = rewardItemLocationRepository.findByLocationId(locationId);
        List<RewardItemStockResponse> lowStockItems = new ArrayList<>();

        for (RewardItemLocation ril : rilList) {
            if (ril.getStock() < threshold) {
                RewardItem rewardItem = ril.getRewardItem();
                List<String> imageUrls = rewardItem.getRewardItemUrl() != null ? rewardItem.getRewardItemUrl() : new ArrayList<>();

                lowStockItems.add(RewardItemStockResponse.builder()
                        .rewardItemId(rewardItem.getId())
                        .rewardItemName(rewardItem.getItemName())
                        .rewardItemDescription(rewardItem.getItemDescription())
                        .rewardItemImageUrl(imageUrls)
                        .stock(ril.getStock())
                        .importing(ril.getImporting())
                        .pending(ril.getPending())
                        .build());
            }
        }
        return lowStockItems;
    }

    @Transactional
    public List<RewardItemStockResponse> getStockByLocation(Long locationId) {
        Locations location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa điểm"));

        List<RewardItemLocation> rilList = rewardItemLocationRepository.findByLocationId(locationId);

        List<RewardItemStockResponse> responses = new ArrayList<>();

        for (RewardItemLocation ril : rilList) {
            RewardItem rewardItem = ril.getRewardItem();

            List<String> imageUrls = rewardItem.getRewardItemUrl() != null
                    ? rewardItem.getRewardItemUrl()
                    : new ArrayList<>();

            responses.add(RewardItemStockResponse.builder()
                    .rewardItemId(rewardItem.getId())
                    .rewardItemName(rewardItem.getItemName())
                    .rewardItemImageUrl(imageUrls)
                    .rewardItemDescription(rewardItem.getItemDescription())
                    .stock(ril.getStock())
                    .importing(ril.getImporting())
                    .pending(ril.getPending())
                    .build());
        }

        return responses;
    }
    @Transactional
    public List<RewardItemStockResponse> getAllStockAcrossLocations() {
        List<RewardItemLocation> rilList = rewardItemLocationRepository.findAll();
        List<RewardItemStockResponse> responses = new ArrayList<>();

        for (RewardItemLocation ril : rilList) {
            RewardItem rewardItem = ril.getRewardItem();
            Locations location = ril.getLocation();

            List<String> imageUrls = rewardItem.getRewardItemUrl() != null
                    ? rewardItem.getRewardItemUrl()
                    : new ArrayList<>();

            responses.add(RewardItemStockResponse.builder()
                    .rewardItemId(rewardItem.getId())
                    .rewardItemName(rewardItem.getItemName())
                    .rewardItemDescription(rewardItem.getItemDescription())
                    .rewardItemImageUrl(imageUrls)
                    .stock(ril.getStock())
                    .importing(ril.getImporting())
                    .pending(ril.getPending())
                    .locationId(location.getId())
                    .locationName(location.getLocationName())
                    .build());
        }

        return responses;
    }
    @Transactional
    public List<RewardItemStockResponse> getStockByRewardItem(Long rewardItemId) {
        RewardItem rewardItem = rewardItemRepository.findById(rewardItemId)
                .orElseThrow(() -> new RuntimeException("Reward item not found"));

        List<RewardItemLocation> rilList = rewardItemLocationRepository.findByRewardItemId(rewardItemId);
        List<RewardItemStockResponse> responses = new ArrayList<>();

        for (RewardItemLocation ril : rilList) {
            Locations location = ril.getLocation();
            List<String> imageUrls = rewardItem.getRewardItemUrl() != null
                    ? rewardItem.getRewardItemUrl()
                    : new ArrayList<>();

            responses.add(RewardItemStockResponse.builder()
                    .rewardItemId(rewardItem.getId())
                    .rewardItemName(rewardItem.getItemName())
                    .rewardItemDescription(rewardItem.getItemDescription())
                    .rewardItemImageUrl(imageUrls)
                    .stock(ril.getStock())
                    .importing(ril.getImporting())
                    .pending(ril.getPending())
                    .locationId(location.getId())
                    .locationName(location.getLocationName())
                    .build());
        }

        return responses;
    }


}
