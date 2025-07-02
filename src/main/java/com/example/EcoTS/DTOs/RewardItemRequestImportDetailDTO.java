package com.example.EcoTS.DTOs;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RewardItemRequestImportDetailDTO {
    private Long id;
    private Long rewardItemId;
    private String rewardItemName;
    private Long numberOfItem;
}
