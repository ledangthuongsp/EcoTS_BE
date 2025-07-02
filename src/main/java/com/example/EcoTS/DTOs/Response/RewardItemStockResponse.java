package com.example.EcoTS.DTOs.Response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RewardItemStockResponse {
    private Long rewardItemId;
    private String rewardItemName;
    private String itemImageUrl;
    private Long stock;
    private Long importing;
    private Long pending;
}
