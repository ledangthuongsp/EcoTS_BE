package com.example.EcoTS.DTOs.Response;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RewardOrderHistoryResponse {
    private Long orderId;
    private String rewardItemName;
    private String locationName;
    private String status;
    private Timestamp createdAt;
    private String imageUrl;
}