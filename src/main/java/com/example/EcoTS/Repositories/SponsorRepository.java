package com.example.EcoTS.Repositories;

import com.example.EcoTS.DTOs.Response.Sponsor.SponsorResponse;
import com.example.EcoTS.Models.Sponsor;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
@Hidden
public interface SponsorRepository extends JpaRepository<Sponsor, Long> {
    Sponsor findByCompanyUsername(String username);
}
