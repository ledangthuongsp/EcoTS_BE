package com.example.EcoTS.Controllers.Reward;

import com.example.EcoTS.DTOs.Response.RewardItemStockResponse;
import com.example.EcoTS.Services.RewardItem.RewardItemLocationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reward")
@RequiredArgsConstructor
@Tag(name = "Reward Import API")
public class RewardItemLocationController {

    @Autowired
    private RewardItemLocationService rewardItemLocationService;

    /**
     * GET /api/reward-items/location/{locationId}/stock
     * Trả về danh sách reward item và số lượng tồn kho tại địa điểm
     */
    @GetMapping("/employee/get-location-stock")
    public ResponseEntity<List<RewardItemStockResponse>> getRewardItemsByLocation(@RequestParam Long locationId) {
        List<RewardItemStockResponse> response = rewardItemLocationService.getStockByLocation(locationId);
        return ResponseEntity.ok(response);
    }

    //    @GetMapping("/low-stock")
    //    public ResponseEntity<List<RewardItemStockResponse>> getLowStockItems(
    //            @RequestParam Long locationId,
    //            @RequestParam(defaultValue = "3") Long threshold
    //    ) {
    //        return ResponseEntity.ok(rewardItemLocationService.getLowStockItemsByLocation(locationId, threshold));
    //    }
    @GetMapping("/stock/all")
    public ResponseEntity<List<RewardItemStockResponse>> getAllStock() {
        return ResponseEntity.ok(rewardItemLocationService.getAllStockAcrossLocations());
    }
    // [GET] /reward/stock/by-reward?rewardItemId=123
    @GetMapping("/stock/by-reward")
    public ResponseEntity<List<RewardItemStockResponse>> getStockByRewardItem(@RequestParam Long rewardItemId) {
        return ResponseEntity.ok(rewardItemLocationService.getStockByRewardItem(rewardItemId));
    }
    @GetMapping("/low-stock")
    public ResponseEntity<List<RewardItemStockResponse>> getLocationsWithLowStockForRewardItem(
            @RequestParam Long rewardItemId, @RequestParam Long threshold) {
        List<RewardItemStockResponse> lowStockItems = rewardItemLocationService.getLocationsWithLowStockForRewardItem(rewardItemId, threshold);
        return ResponseEntity.ok(lowStockItems);
    }

}
