package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.Tokens;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import io.swagger.v3.oas.annotations.Hidden;

@Repository
@RepositoryRestResource
@Hidden
public interface TokenRepository extends JpaRepository<Tokens, Long> {

}