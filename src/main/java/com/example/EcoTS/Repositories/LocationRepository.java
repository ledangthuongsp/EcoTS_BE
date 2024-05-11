package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.Locations;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Locations, Long> {
    List<Locations> findByTypeOfLocation(String type);
}
