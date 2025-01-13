package com.example.EcoTS.Services.Newsfeed;

import com.example.EcoTS.DTOs.Response.Newsfeed.ReactResponse;
import com.example.EcoTS.Models.Newsfeed.Comment;
import com.example.EcoTS.Models.Newsfeed.Newsfeed;
import com.example.EcoTS.Models.Newsfeed.React;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.Newsfeed.CommentRepository;
import com.example.EcoTS.Repositories.Newsfeed.NewsfeedRepository;
import com.example.EcoTS.Repositories.Newsfeed.ReactRepository;
import com.example.EcoTS.Repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class ReactService {
    @Autowired
    private ReactRepository reactRepository;

    @Autowired
    private NewsfeedRepository newsfeedRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    // Add a reaction to a newsfeed
    public ReactResponse addReactToNewsfeed(Long newsfeedId, Long userId) {
        Newsfeed newsfeed = newsfeedRepository.findById(newsfeedId)
                .orElseThrow(() -> new RuntimeException("Newsfeed not found"));

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        React existingReact = reactRepository.findByNewsfeedAndUser(newsfeed, user);
        if (existingReact != null) {
            throw new RuntimeException("User has already reacted to this newsfeed");
        }

        React react = new React();
        react.setNewsfeed(newsfeed);
        react.setUser(user);

        reactRepository.save(react);

        return mapToResponseDTO(react);
    }

    // Add a reaction to a comment
    public ReactResponse addReactToComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        React existingReact = reactRepository.findByCommentAndUser(comment, user);
        if (existingReact != null) {
            throw new RuntimeException("User has already reacted to this comment");
        }

        React react = new React();
        react.setComment(comment);
        react.setUser(user);

        reactRepository.save(react);

        return mapToResponseDTO(react);
    }

    // Remove a reaction (unreact)
    public void removeReact(Long reactId) {
        React react = reactRepository.findById(reactId)
                .orElseThrow(() -> new RuntimeException("React not found"));

        reactRepository.delete(react);
    }

    // Helper method to map React entity to Response DTO
    private ReactResponse mapToResponseDTO(React react) {
        return modelMapper.map(react, ReactResponse.class);
    }
}
