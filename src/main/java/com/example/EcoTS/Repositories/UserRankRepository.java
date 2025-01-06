package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.UserRank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRankRepository extends JpaRepository<UserRank, Long> {
    Optional<UserRank> findByUserId(Long userId);
}
