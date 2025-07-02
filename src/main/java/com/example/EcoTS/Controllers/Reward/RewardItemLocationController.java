package com.example.EcoTS.Controllers.Reward;

import com.example.EcoTS.DTOs.Response.RewardItemStockResponse;
import com.example.EcoTS.Services.RewardItem.RewardItemLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reward/employee")
@RequiredArgsConstructor
public class RewardItemLocationController {

    @Autowired
    private RewardItemLocationService rewardItemLocationService;

    /**
     * GET /api/reward-items/location/{locationId}/stock
     * Trả về danh sách reward item và số lượng tồn kho tại địa điểm
     */
    @GetMapping("/get-location-stock")
    public ResponseEntity<List<RewardItemStockResponse>> getRewardItemsByLocation(@PathVariable Long locationId) {
        List<RewardItemStockResponse> response = rewardItemLocationService.getStockByLocation(locationId);
        return ResponseEntity.ok(response);
    }
}
