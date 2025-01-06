package com.example.EcoTS.Services.UserService;

import com.example.EcoTS.Models.Rank;
import com.example.EcoTS.Models.UserRank;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.RankRepository;
import com.example.EcoTS.Repositories.UserRankRepository;
import com.example.EcoTS.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RankService {

    private final UserRepository userRepository;
    private final UserRankRepository userRankRepository;
    private final RankRepository rankRepository;

    // Quy đổi điểm và cập nhật rank
    public void contributePoints(Long userId, double contributedPoints) {
        // Tìm User và UserRank hiện tại
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserRank userRank = userRankRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User rank not found"));

        // Quy đổi điểm (100 điểm quyên góp -> 10 rank points)
        double rankPointsGained = contributedPoints / 10;
        userRank.setUserRankPoint(userRank.getUserRankPoint() + rankPointsGained);
        userRankRepository.save(userRank);

        // Kiểm tra và cập nhật rank nếu cần
        updateRankIfNeeded(userRank);
    }

    // Hàm kiểm tra và cập nhật rank của người dùng
    private void updateRankIfNeeded(UserRank userRank) {
        double currentRankPoints = userRank.getUserRankPoint();
        Rank currentRank = userRank.getRank();

        Rank newRank;
        if (currentRankPoints >= 100) {
            newRank = rankRepository.findByRankName("Gold")
                    .orElseThrow(() -> new RuntimeException("Rank Gold not found"));
        } else if (currentRankPoints >= 50) {
            newRank = rankRepository.findByRankName("Silver")
                    .orElseThrow(() -> new RuntimeException("Rank Silver not found"));
        } else {
            newRank = rankRepository.findByRankName("Bronze")
                    .orElseThrow(() -> new RuntimeException("Rank Bronze not found"));
        }

        // Cập nhật rank nếu rank mới khác rank hiện tại
        if (!newRank.equals(currentRank)) {
            userRank.setRank(newRank);
            userRankRepository.save(userRank);
        }
    }
}

