package com.example.EcoTS.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class RewardImportRequestDTO {
    private Long locationId;
    private List<RewardImportDetailDTO> itemDetails;
}
