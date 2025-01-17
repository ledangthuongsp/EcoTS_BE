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



    // Helper method to map React entity to Response DTO
    private ReactResponse mapToResponseDTO(React react) {
        return modelMapper.map(react, ReactResponse.class);
    }
}
