package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.ReceiveHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReceiveHistoryRepository extends JpaRepository<ReceiveHistory, Long> {
    List<ReceiveHistory> findAllByUserId(Long userId);
}
