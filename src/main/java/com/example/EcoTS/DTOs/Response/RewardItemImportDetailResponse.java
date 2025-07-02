package com.example.EcoTS.DTOs.Response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RewardItemImportDetailResponse {
    private Long rewardItemId;
    private String rewardItemName;
    private Long numberOfItem;
    private String itemImageUrl;
}
