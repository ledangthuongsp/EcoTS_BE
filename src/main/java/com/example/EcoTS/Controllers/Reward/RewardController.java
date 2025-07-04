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
    @GetMapping("/get-all-reward")
    public List<RewardItem> getAllRewards() {
        return rewardItemService.getAllRewards();
    }

    @PostMapping("/add-new-reward")
    public RewardItem createReward(@RequestBody RewardItem rewardItem) {
        return rewardItemService.createReward(rewardItem);
    }

    @PutMapping("/update-reward/{id}")
    public ResponseEntity<RewardItem> updateReward(@PathVariable Long id, @RequestBody RewardItem updatedReward) {
        try {
            RewardItem rewardItem = rewardItemService.updateReward(id, updatedReward);
            return ResponseEntity.ok(rewardItem);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete-reward/{id}")
    public ResponseEntity<Void> deleteReward(@PathVariable Long id) {
        rewardItemService.deleteReward(id);
        return ResponseEntity.noContent().build();
    }

}
