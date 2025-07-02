package com.example.EcoTS.DTOs.Response;

import com.example.EcoTS.DTOs.RewardItemRequestImportDetailDTO;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RewardItemImportResponse {
    Long id;
    String importStatus;
    String locationName;
    Timestamp createdAt;
    List<RewardItemImportDetailResponse> items;
}