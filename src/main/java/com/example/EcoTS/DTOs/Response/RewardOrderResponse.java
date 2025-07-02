package com.example.EcoTS.DTOs.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RewardOrderResponse {
    private Long orderId;
    private String rewardItemName;
    private String rewardItemImage;
    private int quantity;
    private String locationName;
    private String status;
    private Timestamp createdAt;
}