package com.example.EcoTS.Services.Newsfeed;

import com.example.EcoTS.DTOs.Response.Newsfeed.CommentResponse;
import com.example.EcoTS.Models.Newsfeed.Comment;
import com.example.EcoTS.Models.Newsfeed.Newsfeed;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.Newsfeed.CommentRepository;
import com.example.EcoTS.Repositories.Newsfeed.NewsfeedRepository;
import com.example.EcoTS.Repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private NewsfeedRepository newsfeedRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    // Add comment to a newsfeed
    public CommentResponse addComment(Long newsfeedId, Long userId, String content) {
        Newsfeed newsfeed = newsfeedRepository.findById(newsfeedId)
                .orElseThrow(() -> new RuntimeException("Newsfeed not found"));

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setNewsfeed(newsfeed);
        comment.setUser(user);
        comment.setContent(content);

        commentRepository.save(comment);

        return mapToResponseDTO(comment);
    }

    // Delete comment by ID
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        commentRepository.delete(comment);
    }

    // Get all comments for a specific newsfeed
    public List<CommentResponse> getCommentsForNewsfeed(Long newsfeedId) {
        List<Comment> comments = commentRepository.findByNewsfeedId(newsfeedId);
        return comments.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Helper method to map Comment entity to Response DTO
    private CommentResponse mapToResponseDTO(Comment comment) {
        return modelMapper.map(comment, CommentResponse.class);
    }
}
