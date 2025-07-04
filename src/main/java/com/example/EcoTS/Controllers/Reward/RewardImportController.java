package com.example.EcoTS.Controllers.Reward;

import com.example.EcoTS.DTOs.Response.RewardItemImportResponse;
import com.example.EcoTS.DTOs.RewardImportRequestDTO;
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

    @PostMapping("admin/request")
    public ResponseEntity<String> createImport(@RequestBody RewardImportRequestDTO dto) {
        rewardImportService.createImport(dto); // phải gọi service để xử lý
        return ResponseEntity.ok("Đã gửi yêu cầu nhập hàng.");
    }


    @PostMapping("employee/confirm")
    public ResponseEntity<String> confirmImport(@RequestParam Long requestId) {
        rewardImportService.confirmImport(requestId);
        return ResponseEntity.ok("Đã xác nhận nhập hàng.");
    }

    @GetMapping("employee/requests")
    public ResponseEntity<List<RewardItemImportResponse>> getRequestsByLocation(@RequestParam Long locationId) {
        return ResponseEntity.ok(rewardImportService.getRequestsByLocationDTO(locationId));
    }

    @PostMapping("employee/cancel-expired")
    public ResponseEntity<String> cancelExpiredImports() {
        rewardImportService.cancelStaleImports();
        return ResponseEntity.ok("Đã hủy các đơn nhập quá hạn.");
    }
}