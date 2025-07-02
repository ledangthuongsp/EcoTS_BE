package com.example.EcoTS.DTOs;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder
public class RewardItemRequestImportDTO {
    Long id;
    String importStatus;
    String locationName;
    Timestamp createdAt;
    List<RewardItemRequestImportDetailDTO> items;
}

