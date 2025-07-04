package com.example.EcoTS.Controllers.Reward;

import com.example.EcoTS.Models.Reward.RewardHistory;
import com.example.EcoTS.Models.Reward.RewardItem;
import com.example.EcoTS.Services.RewardItem.RewardItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PutMapping(value = "/update-reward/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RewardItem> updateReward(
            @PathVariable Long id,
            @RequestPart("reward") RewardItem updatedReward,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        try {
            RewardItem rewardItem = rewardItemService.updateReward(id, updatedReward, files);
            return ResponseEntity.ok(rewardItem);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @DeleteMapping("/delete-reward/{id}")
    public ResponseEntity<Void> deleteReward(@PathVariable Long id) {
        rewardItemService.deleteReward(id);
        return ResponseEntity.noContent().build();
    }

}
