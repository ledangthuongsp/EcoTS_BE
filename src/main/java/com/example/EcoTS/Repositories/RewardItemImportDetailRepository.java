package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.Reward.RewardItemImport;
import com.example.EcoTS.Models.Reward.RewardItemImportDetail;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
@Hidden
public interface RewardItemImportDetailRepository extends JpaRepository<RewardItemImportDetail, Long> {
    List<RewardItemImportDetail> findByRequestImport(RewardItemImport requestImport);
}
