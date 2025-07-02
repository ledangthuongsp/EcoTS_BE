package com.example.EcoTS.Utils;

import com.example.EcoTS.Services.RewardItem.RewardOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderScheduler {

    private final RewardOrderService rewardOrderService;

    @Scheduled(cron = "0 0 2 * * *") // Chạy lúc 2h sáng hàng ngày
    public void runAutoExpireOrders() {
        rewardOrderService.autoExpireOrders();
    }
}

