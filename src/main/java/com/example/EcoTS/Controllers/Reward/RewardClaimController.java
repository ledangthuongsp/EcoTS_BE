package com.example.EcoTS.Controllers.Reward;

import com.example.EcoTS.Models.Reward.RewardItemClaim;
import com.example.EcoTS.Services.RewardItem.RewardClaimService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reward/claim")
@RequiredArgsConstructor
@Tag(name = "Reward Claim API")
public class RewardClaimController {
    private final RewardClaimService rewardClaimService;

    @PostMapping
    public ResponseEntity<String> claimReward(
            @RequestParam Long userId,
            @RequestParam Long rewardItemId,
            @RequestParam Long locationId) {
        rewardClaimService.claimReward(userId, rewardItemId, locationId);
        return ResponseEntity.ok("Đổi thưởng thành công, đang chờ nhận.");
    }

    @PostMapping("/confirm")
    public ResponseEntity<String> confirmClaim(@RequestParam Long claimId) {
        rewardClaimService.confirmClaim(claimId);
        return ResponseEntity.ok("Đã xác nhận đơn nhận thưởng.");
    }

    @GetMapping("/history")
    public ResponseEntity<List<RewardItemClaim>> getUserClaimHistory(@RequestParam Long userId) {
        return ResponseEntity.ok(rewardClaimService.getUserClaims(userId));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<RewardItemClaim>> getPendingClaims(@RequestParam Long locationId) {
        return ResponseEntity.ok(rewardClaimService.getPendingClaimsAtLocation(locationId));
    }

    @PostMapping("/cancel-expired")
    public ResponseEntity<String> cancelExpiredClaims() {
        rewardClaimService.cancelExpiredClaims();
        return ResponseEntity.ok("Đã hủy các đơn quá hạn.");
    }
}
