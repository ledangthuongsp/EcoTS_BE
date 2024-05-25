package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.Locations;
import com.example.EcoTS.Models.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Reviews, Long> {
    List<Reviews> findByLocation(Locations location);
}
