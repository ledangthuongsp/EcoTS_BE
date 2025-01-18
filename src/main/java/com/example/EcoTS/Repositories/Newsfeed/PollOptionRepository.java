package com.example.EcoTS.Repositories.Newsfeed;

import com.example.EcoTS.Models.Newsfeed.PollOption;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Hidden
public interface PollOptionRepository extends JpaRepository<PollOption, Long> {
    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END " +
            "FROM Vote v WHERE v.userId = :userId AND v.id IN :voteIds")
    boolean existsByUserIdAndVoteIdsContaining(@Param("userId") Long userId, @Param("voteIds") List<Long> voteIds);
}
