package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.ReceiveHistory;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Hidden
public interface ReceiveHistoryRepository extends JpaRepository<ReceiveHistory, Long> {
    List<ReceiveHistory> findAllByUserId(Long userId);
}
