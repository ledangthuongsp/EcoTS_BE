package com.example.EcoTS.Repositories.Newsfeed;

import com.example.EcoTS.Models.Newsfeed.Newsfeed;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Hidden
public interface NewsfeedRepository extends JpaRepository<Newsfeed, Long> {
    List<Newsfeed> findByUserId(Long userId);
    List<Newsfeed> findBySponsorId(Long sponsorId);
    // Method to fetch all newsfeeds ordered by creation time, latest first
    List<Newsfeed> findAllByOrderByCreatedAtDesc();

    // Method to fetch newsfeeds for a specific user, ordered by creation time
    List<Newsfeed> findByUserIdOrderByCreatedAtDesc(Long userId);

}
