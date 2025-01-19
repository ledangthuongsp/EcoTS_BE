package com.example.EcoTS.Repositories.Newsfeed;

import com.example.EcoTS.Models.Newsfeed.Comment;
import com.example.EcoTS.Models.Newsfeed.Newsfeed;
import com.example.EcoTS.Models.Newsfeed.React;
import com.example.EcoTS.Models.Users;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
@Hidden
public interface ReactRepository extends JpaRepository<React, Long> {

    React findByUserId (Long userId);
    List<React> findAllByUserId(Long userId);
    Optional<React> findByUserIdAndNewsfeedId(Long userId, Long newsfeedId);
}
