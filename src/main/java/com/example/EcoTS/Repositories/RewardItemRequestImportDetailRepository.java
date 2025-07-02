package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.Reward.RewardItemRequestImport;
import com.example.EcoTS.Models.Reward.RewardItemRequestImportDetail;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
@Hidden
public interface RewardItemRequestImportDetailRepository extends JpaRepository<RewardItemRequestImportDetail, Long> {
    List<RewardItemRequestImportDetail> findByRequestImport(RewardItemRequestImport requestImport);
}
