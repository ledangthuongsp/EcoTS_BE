package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.Materials;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import io.swagger.v3.oas.annotations.Hidden;

@Repository
@RepositoryRestResource
@Hidden
public interface MaterialRepository extends JpaRepository<Materials, Long> {
    Optional<Materials> findByName (String name);
}
