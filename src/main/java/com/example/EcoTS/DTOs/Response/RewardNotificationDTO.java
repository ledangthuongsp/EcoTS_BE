package com.example.EcoTS.DTOs.Response;

import com.example.EcoTS.Enum.RewardOrderStatus;
import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
@Getter
@Setter
public class RewardNotificationDTO {
    private Long id;
    private String title;
    private String description;
    private Long userId;
    private String rewardItemName;
    private int quantity;
    private String locationName;
    private RewardOrderStatus status;
    private boolean isRead;
    private Timestamp createdAt;
}