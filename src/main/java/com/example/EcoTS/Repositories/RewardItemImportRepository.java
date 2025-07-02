package com.example.EcoTS.Repositories;

import com.example.EcoTS.Enum.ImportStatus;
import com.example.EcoTS.Models.Locations;
import com.example.EcoTS.Models.Reward.RewardItemImport;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;
@Hidden
public interface RewardItemImportRepository extends JpaRepository<RewardItemImport, Long> {
    List<RewardItemImport> findByImportStatusAndCreatedAtBefore(ImportStatus status, Timestamp before);
    List<RewardItemImport> findByLocationId(Long locationId);
    List<RewardItemImport> findByLocation(Locations location);
}
