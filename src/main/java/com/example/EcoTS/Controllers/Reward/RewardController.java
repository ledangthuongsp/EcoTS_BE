package com.example.EcoTS.Controllers.Reward;

import com.example.EcoTS.Models.Reward.RewardItem;
import com.example.EcoTS.Services.RewardItem.RewardItemService;
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
}
