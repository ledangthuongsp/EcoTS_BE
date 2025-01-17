package com.example.EcoTS.Services.Newsfeed;

import com.example.EcoTS.DTOs.Request.Newsfeed.CommentRequest;
import com.example.EcoTS.DTOs.Response.Newsfeed.CommentResponse;
import com.example.EcoTS.Models.Newsfeed.Comment;
import com.example.EcoTS.Models.Newsfeed.Newsfeed;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.Newsfeed.CommentRepository;
import com.example.EcoTS.Repositories.Newsfeed.NewsfeedRepository;
import com.example.EcoTS.Repositories.UserRepository;
import com.example.EcoTS.Services.CloudinaryService.CloudinaryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @Autowired
    private CloudinaryService cloudinaryService;

    public void createNewCommment (CommentRequest commentRequest, List<MultipartFile> imgUrls) throws IOException {
        Comment comment = new Comment();
        comment.setMessage(commentRequest.getMessage());
        List<String> images = cloudinaryService.uploadMultipleCommetImage(imgUrls);
        comment.setImgUrls(images);
        comment.setUserId(commentRequest.getUserId());
        commentRepository.save(comment);
    }
    public List<Comment> getAllComment()
    {
        return commentRepository.findAll();
    }
    public void deleteComment(Long commmentId)
    {
        commentRepository.deleteById(commmentId);
    }
}
