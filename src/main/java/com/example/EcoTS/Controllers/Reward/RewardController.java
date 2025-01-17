package com.example.EcoTS.Controllers.Reward;

import com.example.EcoTS.Models.Reward.RewardHistory;
import com.example.EcoTS.Models.Reward.RewardItem;
import com.example.EcoTS.Services.RewardItem.RewardItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reward")
@CrossOrigin
@Tag(name = "Reward Item API")
public class RewardController {
    @Autowired
    private RewardItemService rewardItemService;

    @GetMapping
    public List<RewardItem> getAllRewards() {
        return rewardItemService.getAllRewards();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RewardItem> getRewardById(@PathVariable Long id) {
        Optional<RewardItem> rewardItem = rewardItemService.getRewardById(id);
        return rewardItem.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public RewardItem createReward(@RequestBody RewardItem rewardItem) {
        return rewardItemService.createReward(rewardItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RewardItem> updateReward(@PathVariable Long id, @RequestBody RewardItem updatedReward) {
        try {
            RewardItem rewardItem = rewardItemService.updateReward(id, updatedReward);
            return ResponseEntity.ok(rewardItem);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReward(@PathVariable Long id) {
        rewardItemService.deleteReward(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update-reward-history")
    @Operation(summary = "Update Reward History", description = "Tự động cập nhật điểm, trừ stock, lưu lại lịch sử nếu điều kiện hợp lệ.")
    public ResponseEntity<String> updateRewardHistory(
            @RequestParam Long userId,
            @RequestParam double point,
            @RequestParam Long rewardItemId,
            @RequestParam Long numberOfItem,
            @RequestParam Long locationId) {

        try {
            rewardItemService.updateHistoryCharge(userId, point, rewardItemId, numberOfItem, locationId);
            return ResponseEntity.ok("Reward history updated successfully.");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(500).body("Internal server error: " + ex.getMessage());
        }
    }

    @GetMapping("/get-reward-history")
    public ResponseEntity<List<RewardHistory>> getRewardHistory(@RequestParam Long userId) {
        List<RewardHistory> rewardHistory = rewardItemService.getAllRewardHistoryById(userId);

        if (rewardHistory == null || rewardHistory.isEmpty()) {
            return ResponseEntity.notFound().build(); // Trả về 404 nếu không tìm thấy dữ liệu
        }

        return ResponseEntity.ok(rewardHistory); // Trả về 200 với danh sách dữ liệu
    }

}
