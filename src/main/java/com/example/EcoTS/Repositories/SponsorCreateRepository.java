package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.SponsorCreate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SponsorCreateRepository extends JpaRepository<SponsorCreate, Long> {
    List<SponsorCreate> findByStatus(SponsorCreate.Status status);
    Optional<SponsorCreate> findByEmail(String email);
}
