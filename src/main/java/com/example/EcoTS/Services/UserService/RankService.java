package com.example.EcoTS.Services.UserService;

import com.example.EcoTS.Models.Rank;
import com.example.EcoTS.Models.UserRank;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.RankRepository;
import com.example.EcoTS.Repositories.UserRankRepository;
import com.example.EcoTS.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankService {

    private final UserRepository userRepository;
    private final UserRankRepository userRankRepository;
    private final RankRepository rankRepository;

    public List<Rank> getAllRanks() {
        return rankRepository.findAll();
    }

    // Thêm rank mới

    @Transactional
    public void addRank(double rankPoint, String rankName) {
        Rank rank = new Rank();
        rank.setRankPoint(rankPoint);
        rank.setRankName(rankName);
        rankRepository.save(rank);
    }

    // Sửa rank hiện tại
    @Transactional
    public void updateRank(Long rankId, Rank updatedRank) {
        Rank rank = rankRepository.findById(rankId)
                .orElseThrow(() -> new RuntimeException("Rank not found"));

        rank.setRankName(updatedRank.getRankName());
        rank.setRankPoint(updatedRank.getRankPoint());

        rankRepository.save(rank);
    }

    // Xóa rank
    @Transactional
    public void deleteRank(Long rankId) {
        Rank rank = rankRepository.findById(rankId)
                .orElseThrow(() -> new RuntimeException("Rank not found"));

        rankRepository.delete(rank);
    }

    // Quy đổi điểm và cập nhật rank cho người dùng
    @Transactional
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

    // Cập nhật rank của người dùng nếu cần
    @Transactional
    private void updateRankIfNeeded(UserRank userRank) {
        double currentRankPoints = userRank.getUserRankPoint();
        Rank currentRank = userRank.getRank();

        Rank newRank;
        if (currentRankPoints >= 1000000) {
            newRank = rankRepository.findByRankName("Diamond")
                    .orElseThrow(() -> new RuntimeException("Rank Diamond not found"));
        } else if (currentRankPoints >= 50000) {
            newRank = rankRepository.findByRankName("Platinum")
                    .orElseThrow(() -> new RuntimeException("Rank Platinum not found"));
        } else if (currentRankPoints >= 25000) {
            newRank = rankRepository.findByRankName("Gold")
                    .orElseThrow(() -> new RuntimeException("Rank Gold not found"));
        } else if (currentRankPoints >= 10000) {
            newRank = rankRepository.findByRankName("Silver")
                    .orElseThrow(() -> new RuntimeException("Rank Silver not found"));
        } else if (currentRankPoints >= 5000) {
            newRank = rankRepository.findByRankName("Bronze")
                    .orElseThrow(() -> new RuntimeException("Rank Bronze not found"));
        } else {
            newRank = rankRepository.findByRankName("Iron")
                    .orElseThrow(() -> new RuntimeException("Rank Iron not found"));
        }

        // Cập nhật rank nếu rank mới khác rank hiện tại
        if (!newRank.equals(currentRank)) {
            userRank.setRank(newRank);
            userRankRepository.save(userRank);
        }
    }

    // Lấy thông tin rank của người dùng
    @Transactional
    public UserRank getUserRank(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Tạo mới UserRank nếu không tồn tại
        return userRankRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Rank defaultRank = rankRepository.findByRankName("Iron")
                            .orElseThrow(() -> new RuntimeException("Default rank Iron not found"));

                    UserRank newUserRank = UserRank.builder()
                            .user(user)
                            .rank(defaultRank)
                            .userRankPoint(0.0)
                            .build();
                    return userRankRepository.save(newUserRank);
                });
    }

}
