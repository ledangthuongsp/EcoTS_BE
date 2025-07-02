package com.example.EcoTS.Services.RewardItem;

import com.example.EcoTS.DTOs.RewardItemRequestImportDTO;
import com.example.EcoTS.DTOs.RewardItemRequestImportDetailDTO;
import com.example.EcoTS.Enum.ImportStatus;
import com.example.EcoTS.Models.Locations;
import com.example.EcoTS.Models.Reward.*;
import com.example.EcoTS.Repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RewardImportService {
    private final RewardItemRequestImportRepository requestRepo;
    private final RewardItemRequestImportDetailRepository detailRepo;
    private final RewardItemLocationRepository locationRepo;
    private final LocationRepository locationRepository;

    @Transactional
    public void createImportRequest(Long locationId, List<RewardItemRequestImportDetail> details) {
        Locations location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa điểm"));

        RewardItemRequestImport request = RewardItemRequestImport.builder()
                .location(location)
                .importStatus(ImportStatus.IMPORTING)
                .build();
        requestRepo.save(request);

        for (RewardItemRequestImportDetail detail : details) {
            detail.setRequestImport(request);
            detailRepo.save(detail);
        }
    }

    @Transactional
    public void confirmImport(Long requestId) {
        RewardItemRequestImport request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu nhập hàng"));

        if (request.getImportStatus() != ImportStatus.IMPORTING)
            throw new RuntimeException("Yêu cầu chưa đến bước xác nhận");

        List<RewardItemRequestImportDetail> items = detailRepo.findByRequestImport(request);

        for (RewardItemRequestImportDetail detail : items) {
            RewardItem reward = detail.getRewardItem();
            Locations location = request.getLocation();

            RewardItemLocation ril = locationRepo.findByRewardItemAndLocation(reward, location)
                    .orElseGet(() -> RewardItemLocation.builder()
                            .location(location)
                            .rewardItem(reward)
                            .stock(0L)
                            .pending(0L)
                            .build());

            ril.setStock(ril.getStock() + detail.getNumberOfItem());
            locationRepo.save(ril);
        }

        request.setImportStatus(ImportStatus.CONFIRMED);
        requestRepo.save(request);
    }

    @Transactional
    public List<RewardItemRequestImportDTO> getRequestsByLocationDto(Long locationId) {
        return requestRepo.findByLocationId(locationId).stream().map(request -> {
            // Lấy danh sách chi tiết từng reward trong đơn
            List<RewardItemRequestImportDetailDTO> itemDtos = detailRepo.findByRequestImport(request).stream().map(detail ->
                    RewardItemRequestImportDetailDTO.builder()
                            .id(detail.getId())
                            .rewardItemId(detail.getRewardItem().getId())
                            .numberOfItem(detail.getNumberOfItem())
                            .build()
            ).toList();

            // Tạo DTO chính cho đơn nhập
            return RewardItemRequestImportDTO.builder()
                    .id(request.getId())
                    .importStatus(request.getImportStatus().name())
                    .createdAt(request.getCreatedAt())
                    .items(itemDtos)
                    .build();
        }).toList();
    }


    @Transactional
    public void cancelStaleImports() {
        Timestamp expiredTime = Timestamp.valueOf(LocalDateTime.now().minusDays(3));
        List<RewardItemRequestImport> expiredRequests =
                requestRepo.findByImportStatusAndCreatedAtBefore(ImportStatus.IMPORTING, expiredTime);

        for (RewardItemRequestImport req : expiredRequests) {
            req.setImportStatus(ImportStatus.CANCELLED);
            requestRepo.save(req);
        }
    }
}