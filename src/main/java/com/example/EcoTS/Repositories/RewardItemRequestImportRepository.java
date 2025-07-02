package com.example.EcoTS.Repositories;

import com.example.EcoTS.Enum.ImportStatus;
import com.example.EcoTS.Models.Reward.RewardItemRequestImport;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;
@Hidden
public interface RewardItemRequestImportRepository extends JpaRepository<RewardItemRequestImport, Long> {
    List<RewardItemRequestImport> findByImportStatusAndCreatedAtBefore(ImportStatus status, Timestamp before);
    List<RewardItemRequestImport> findByLocationId(Long locationId);

}
