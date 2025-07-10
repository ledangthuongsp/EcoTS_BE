package com.example.EcoTS.Repositories;

import com.example.EcoTS.Mappers.DonationMapper;
import com.example.EcoTS.Models.DonationHistory;
import com.example.EcoTS.Models.Donations;
import com.example.EcoTS.Models.Verifications;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Hidden;

@Repository
@RepositoryRestResource
@Hidden
public interface DonationRepository extends JpaRepository<Donations, Long> {
    Optional<Donations> findById(Long id);
    @Query("SELECT d FROM Donations d WHERE d.sponsor.id = :sponsorId")
    List<Donations> findBySponsorId(@Param("sponsorId") Long sponsorId);

}
