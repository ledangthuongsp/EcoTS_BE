package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.Reward.RewardNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RewardNotificationRepository extends JpaRepository<RewardNotification, Long> {
    // Lấy thông báo chưa đọc của người dùng
    List<RewardNotification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(Long userId);

    // Lấy tất cả thông báo của người dùng
    List<RewardNotification> findByUserIdOrderByCreatedAtDesc(Long userId);
}
