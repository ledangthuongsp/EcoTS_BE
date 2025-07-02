package com.example.EcoTS.Utils;

import com.example.EcoTS.Services.RewardItem.RewardClaimService;
import com.example.EcoTS.Services.RewardItem.RewardImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RewardScheduler {
    private final RewardClaimService rewardClaimService;
    private final RewardImportService rewardImportService;

    @Scheduled(cron = "0 0 * * * *") // chạy mỗi giờ
    public void autoCleanup() {
        rewardClaimService.cancelExpiredClaims();
        rewardImportService.cancelStaleImports();
    }
}

