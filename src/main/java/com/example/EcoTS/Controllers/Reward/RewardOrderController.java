package com.example.EcoTS.Controllers.Reward;

import com.example.EcoTS.DTOs.Response.RewardOrderResponse;
import com.example.EcoTS.Services.RewardItem.RewardOrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reward/redeem-reward")
@RequiredArgsConstructor
@Tag(name = "API for user claim reward")
public class RewardOrderController {

    private final RewardOrderService rewardOrderService;

    @PostMapping("/user/redeem")
    public ResponseEntity<String> redeemReward(
            @RequestParam Long userId,
            @RequestParam Long rewardItemId,
            @RequestParam Long locationId,
            @RequestParam int quantity
    ) {
        rewardOrderService.redeemReward(userId, rewardItemId, locationId, quantity);
        return ResponseEntity.ok("Redeem successful");
    }

    @GetMapping("/user/history")
    public ResponseEntity<List<RewardOrderResponse>> getUserRewardHistory(@RequestParam Long userId) {
        return ResponseEntity.ok(rewardOrderService.getUserRewardHistory(userId));
    }

    @GetMapping("/employee/get-order-location")
    public ResponseEntity<List<RewardOrderResponse>> getOrdersByLocation(@RequestParam Long locationId) {
        return ResponseEntity.ok(rewardOrderService.getOrdersByLocationId(locationId));
    }

    @PutMapping("/employee/order/confirm")
    public ResponseEntity<?> confirmOrder(@RequestParam Long orderId) {
        rewardOrderService.confirmOrder(orderId);
        return ResponseEntity.ok("Order confirmed successfully");
    }
    @PutMapping("/user/order/confirm")
    public ResponseEntity<?> userConfirmOrder(@RequestParam Long orderId) {
        rewardOrderService.userConfirmOrder(orderId);
        return ResponseEntity.ok("Order confirmed by user");
    }

    @PutMapping("/user/order/cancel")
    public ResponseEntity<?> cancelOrderByUser(@RequestParam Long orderId) {
        rewardOrderService.cancelOrderByUser(orderId);
        return ResponseEntity.ok("Order cancelled and points refunded");
    }

}