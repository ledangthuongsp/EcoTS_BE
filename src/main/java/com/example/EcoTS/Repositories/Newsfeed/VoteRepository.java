package com.example.EcoTS.Repositories.Newsfeed;

import com.example.EcoTS.Models.Newsfeed.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
}
