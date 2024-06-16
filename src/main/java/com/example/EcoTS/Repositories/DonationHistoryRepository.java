package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.DonationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationHistoryRepository extends JpaRepository<DonationHistory, Long> {
}
