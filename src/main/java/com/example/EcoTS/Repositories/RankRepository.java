package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.Rank;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Hidden
public interface RankRepository extends JpaRepository<Rank, Long> {

    // Tìm Rank theo tên rank
    Optional<Rank> findByRankName(String rankName);
}
