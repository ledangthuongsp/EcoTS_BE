package com.example.EcoTS.Services.RewardItem;

import com.example.EcoTS.Enum.RewardItemClaimStatus;
import com.example.EcoTS.Models.Locations;
import com.example.EcoTS.Models.Points;
import com.example.EcoTS.Models.Reward.RewardItem;
import com.example.EcoTS.Models.Reward.RewardItemClaim;
import com.example.EcoTS.Models.Reward.RewardItemLocation;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RewardClaimService {
    private final RewardItemClaimRepository claimRepo;
    private final RewardItemLocationRepository locationRepo;
    private final LocationRepository locationRepository;
    private final RewardItemRepository rewardRepo;
    private final PointRepository pointRepo;
    private final UserRepository userRepo;

    @Transactional
    public void claimReward(Long userId, Long rewardItemId, Long locationId) {
        Users user = userRepo.findById(userId).orElseThrow();
        RewardItem reward = rewardRepo.findById(rewardItemId).orElseThrow();
        Locations location = locationRepository.findById(locationId).orElseThrow(()-> new RuntimeException("Không tìm thấy location với id:" + locationId));

        Points points = pointRepo.findByUser(user).orElseThrow();
        if (points.getPoint() < reward.getPointCharge()) throw new RuntimeException("Không đủ điểm");

        RewardItemLocation ril = locationRepo.findByRewardItemAndLocation(reward, location)
                .orElseThrow(() -> new RuntimeException("Không có reward ở địa điểm"));

        if (ril.getStock() <= 0) throw new RuntimeException("Hết hàng");

        // Cập nhật stock
        ril.setStock(ril.getStock() - 1);
        ril.setPending(ril.getPending() + 1);
        points.setPoint(points.getPoint() - reward.getPointCharge());

        RewardItemClaim claim = RewardItemClaim.builder()
                .user(user)
                .rewardItem(reward)
                .location(location)
                .status(RewardItemClaimStatus.PENDING)
                .build();
        claimRepo.save(claim);
    }

    @Transactional
    public void confirmClaim(Long claimId) {
        RewardItemClaim claim = claimRepo.findById(claimId).orElseThrow();
        if (claim.getStatus() != RewardItemClaimStatus.PENDING) throw new RuntimeException("Claim không hợp lệ");

        RewardItemLocation ril = locationRepo.findByRewardItemAndLocation(claim.getRewardItem(), claim.getLocation())
                .orElseThrow();
        ril.setPending(ril.getPending() - 1);

        claim.setStatus(RewardItemClaimStatus.COMPLETED);
    }

    @Transactional
    public void cancelExpiredClaims() {
        Timestamp expiredTime = Timestamp.valueOf(LocalDateTime.now().minusDays(3));
        List<RewardItemClaim> expiredClaims = claimRepo.findByStatusAndCreatedAtBefore(
                RewardItemClaimStatus.PENDING, expiredTime
        );

        for (RewardItemClaim claim : expiredClaims) {
            RewardItemLocation ril = locationRepo.findByRewardItemAndLocation(
                    claim.getRewardItem(), claim.getLocation()).orElseThrow();

            ril.setStock(ril.getStock() + 1);
            ril.setPending(ril.getPending() - 1);

            Points point = pointRepo.findByUser(claim.getUser()).orElseThrow();
            point.setPoint(point.getPoint() + claim.getRewardItem().getPointCharge());

            claim.setStatus(RewardItemClaimStatus.CANCELLED);
        }
    }
    public List<RewardItemClaim> getUserClaims(Long userId) {
        return claimRepo.findByUserId(userId);
    }

    public List<RewardItemClaim> getPendingClaimsAtLocation(Long locationId) {
        return claimRepo.findByLocationIdAndStatus(locationId, RewardItemClaimStatus.PENDING);
    }

}

