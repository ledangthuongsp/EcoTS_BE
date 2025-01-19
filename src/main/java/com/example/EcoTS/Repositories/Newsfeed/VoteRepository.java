package com.example.EcoTS.Repositories.Newsfeed;

import com.example.EcoTS.Models.Newsfeed.Vote;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Hidden
public interface VoteRepository extends JpaRepository<Vote, Long> {
}
