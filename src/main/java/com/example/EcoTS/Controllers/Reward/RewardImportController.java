package com.example.EcoTS.Controllers.Reward;

import com.example.EcoTS.DTOs.RewardImportRequestDTO;
import com.example.EcoTS.DTOs.RewardItemRequestImportDTO;
import com.example.EcoTS.Models.Reward.RewardItem;
import com.example.EcoTS.Models.Reward.RewardItemLocation;
import com.example.EcoTS.Models.Reward.RewardItemRequestImport;
import com.example.EcoTS.Models.Reward.RewardItemRequestImportDetail;
import com.example.EcoTS.Services.RewardItem.RewardImportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reward/import")
@RequiredArgsConstructor
@Tag(name = "Reward Import API")
public class RewardImportController {

    private final RewardImportService rewardImportService;

    @PostMapping("/request")
    public ResponseEntity<String> createImportRequest(@RequestBody RewardImportRequestDTO dto) {
        List<RewardItemRequestImportDetail> detailEntities = dto.getItemDetails().stream().map(detailDTO ->
                RewardItemRequestImportDetail.builder()
                        .rewardItem(RewardItem.builder().id(detailDTO.getRewardItemId()).build())
                        .numberOfItem(detailDTO.getNumberOfItem())
                        .build()
        ).toList();

        rewardImportService.createImportRequest(dto.getLocationId(), detailEntities);
        return ResponseEntity.ok("Đã gửi yêu cầu nhập hàng.");
    }


    @PostMapping("/confirm")
    public ResponseEntity<String> confirmImport(@RequestParam Long requestId) {
        rewardImportService.confirmImport(requestId);
        return ResponseEntity.ok("Đã xác nhận nhập hàng.");
    }

    @GetMapping("/requests")
    public ResponseEntity<List<RewardItemRequestImportDTO>> getRequestsByLocation(@RequestParam Long locationId) {
        return ResponseEntity.ok(rewardImportService.getRequestsByLocationDto(locationId));
    }


    @PostMapping("/cancel-expired")
    public ResponseEntity<String> cancelExpiredImports() {
        rewardImportService.cancelStaleImports();
        return ResponseEntity.ok("Đã hủy các đơn nhập quá hạn.");
    }
}