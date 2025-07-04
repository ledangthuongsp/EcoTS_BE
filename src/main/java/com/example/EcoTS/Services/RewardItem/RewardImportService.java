package com.example.EcoTS.Services.RewardItem;

import com.example.EcoTS.DTOs.Response.RewardItemImportDetailResponse;
import com.example.EcoTS.DTOs.Response.RewardItemImportResponse;
import com.example.EcoTS.DTOs.RewardImportDetailDTO;
import com.example.EcoTS.DTOs.RewardImportRequestDTO;
import com.example.EcoTS.Enum.ImportStatus;
import com.example.EcoTS.Models.Locations;
import com.example.EcoTS.Models.Reward.*;
import com.example.EcoTS.Repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RewardImportService {

    @Autowired
    public RewardItemLocationRepository rewardItemLocationRepository;
    @Autowired
    public LocationRepository locationRepository;
    @Autowired
    public RewardItemRepository rewardItemRepository;
    @Autowired
    public RewardItemImportRepository rewardItemImportRepository;
    @Autowired
    public RewardItemImportDetailRepository rewardItemImportDetailRepository;

    @Transactional
    public void createImport(RewardImportRequestDTO dto) {
        Locations location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa điểm"));

        // Tạo đơn nhập chính
        RewardItemImport rewardItemImport = RewardItemImport.builder()
                .location(location)
                .importStatus(ImportStatus.IMPORTING)
                .build();
        rewardItemImportRepository.save(rewardItemImport);

        // Xử lý từng item trong danh sách nhập
        for (RewardImportDetailDTO itemDto : dto.getItemDetails()) {
            RewardItem rewardItem = rewardItemRepository.findById(itemDto.getRewardItemId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy reward item với ID: " + itemDto.getRewardItemId()));

            // Tạo chi tiết nhập hàng
            RewardItemImportDetail detail = RewardItemImportDetail.builder()
                    .requestImport(rewardItemImport)
                    .rewardItem(rewardItem)
                    .numberOfItem(itemDto.getNumberOfItem())
                    .build();

            rewardItemImportDetailRepository.save(detail);

            // Cập nhật số lượng "đang nhập" vào RewardItemLocation
            RewardItemLocation ril = rewardItemLocationRepository
                    .findByRewardItemAndLocation(rewardItem, location)
                    .orElse(null);

            if (ril == null) {
                // Nếu chưa có, tạo bản ghi mới
                ril = RewardItemLocation.builder()
                        .location(location)
                        .rewardItem(rewardItem)
                        .stock(0L)
                        .importing(itemDto.getNumberOfItem())
                        .pending(0L)
                        .build();
            } else {
                // Nếu đã có, chỉ cần cập nhật "importing"
                ril.setImporting(ril.getImporting() + itemDto.getNumberOfItem());
            }
            rewardItemLocationRepository.save(ril);
        }
    }


    @Transactional
    public void confirmImport(Long requestId) {
        RewardItemImport rewardItemImport = rewardItemImportRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu nhập hàng"));

        if (rewardItemImport.getImportStatus() != ImportStatus.IMPORTING)
            throw new RuntimeException("Yêu cầu chưa đến bước xác nhận");

        List<RewardItemImportDetail> items = rewardItemImportDetailRepository.findByRequestImport(rewardItemImport);

        for (RewardItemImportDetail detail : items) {
            RewardItem reward = detail.getRewardItem();
            Locations location = rewardItemImport.getLocation();

            RewardItemLocation ril = rewardItemLocationRepository
                    .findByRewardItemAndLocation(reward, location)
                    .orElseThrow(() -> new RuntimeException("RewardItemLocation không tồn tại"));

            Long quantity = detail.getNumberOfItem();

            ril.setImporting(Math.max(0, ril.getImporting() - quantity));
            ril.setStock(ril.getStock() + quantity);

            rewardItemLocationRepository.save(ril);
        }

        rewardItemImport.setImportStatus(ImportStatus.CONFIRMED);
        rewardItemImportRepository.save(rewardItemImport);
    }


    @Transactional
    public List<RewardItemImportResponse> getRequestsByLocationDTO(Long locationId) {
        Locations location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location is not found"));

        List<RewardItemImport> rewardItemImportList = rewardItemImportRepository.findByLocation(location);
        List<RewardItemImportResponse> rewardItemImportResponseList = new ArrayList<>();

        for (RewardItemImport rewardItemImport : rewardItemImportList) {
            List<RewardItemImportDetail> rewardItemImportDetailList =
                    rewardItemImportDetailRepository.findByRequestImport(rewardItemImport);

            List<RewardItemImportDetailResponse> detailResponses = new ArrayList<>();
            for (RewardItemImportDetail detail : rewardItemImportDetailList) {
                RewardItem rewardItem = detail.getRewardItem();

                // Lấy URL đầu tiên nếu có
                String imageUrl = (rewardItem.getRewardItemUrl() != null && !rewardItem.getRewardItemUrl().isEmpty())
                        ? rewardItem.getRewardItemUrl().get(0)
                        : null;

                detailResponses.add(RewardItemImportDetailResponse.builder()
                        .rewardItemId(rewardItem.getId())
                        .rewardItemName(rewardItem.getItemName())
                        .numberOfItem(detail.getNumberOfItem())
                        .itemImageUrl(imageUrl)
                        .build());
            }

            rewardItemImportResponseList.add(RewardItemImportResponse.builder()
                    .id(rewardItemImport.getId())
                    .locationName(location.getLocationName())
                    .importStatus(rewardItemImport.getImportStatus().toString())
                    .createdAt(rewardItemImport.getCreatedAt())
                    .items(detailResponses)
                    .build());
        }

        return rewardItemImportResponseList;
    }
    @Transactional
    public void cancelStaleImports() {
        // Tính thời điểm cách đây 3 ngày
        Timestamp expiredTime = Timestamp.valueOf(LocalDateTime.now().minusDays(3));

        // Lấy các yêu cầu ở trạng thái IMPORTING và đã tạo trước expiredTime
        List<RewardItemImport> expiredRequests = rewardItemImportRepository
                .findByImportStatusAndCreatedAtBefore(ImportStatus.IMPORTING, expiredTime);

        for (RewardItemImport request : expiredRequests) {
            request.setImportStatus(ImportStatus.CANCELLED);
            rewardItemImportRepository.save(request);
        }
    }

}