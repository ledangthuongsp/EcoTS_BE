package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.Locations;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RepositoryRestResource
@Hidden
public interface LocationRepository extends JpaRepository<Locations, Long> {
    List<Locations> findByTypeOfLocation(String type);
    Optional<Locations> findByEmployeeId(Long employeeId);
}
