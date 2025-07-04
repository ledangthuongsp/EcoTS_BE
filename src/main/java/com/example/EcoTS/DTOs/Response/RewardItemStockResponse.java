package com.example.EcoTS.DTOs.Response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RewardItemStockResponse {
    private Long rewardItemId;
    private String rewardItemName;

    private double pointCharge;
    private String rewardItemDescription;

    private List<String> rewardItemImageUrl;

    private Long locationId;
    private String locationName;

    private Long stock;
    private Long importing;
    private Long pending;
}
