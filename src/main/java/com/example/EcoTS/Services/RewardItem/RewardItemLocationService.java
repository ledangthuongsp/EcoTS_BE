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
    private LocationRepository locationRepository;

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
                    .humidity(rewardItem.getHumidity())
                            .rewardItemType(rewardItem.getItemType())
                            .height(rewardItem.getHeight())
                            .size(rewardItem.getSize())
                            .weight(rewardItem.getWeight())
                    .stock(ril.getStock())
                    .importing(ril.getImporting())
                    .pending(ril.getPending())
                    .build());
        }

        return responses;
    }
}
