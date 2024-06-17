package com.example.EcoTS.Services.UserService;

import com.example.EcoTS.DTOs.Request.User.ChangeInfoRequest;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.*;
import com.example.EcoTS.Services.CloudinaryService.CloudinaryService;
import com.example.EcoTS.Services.SecurityService.JwtService;

import org.apache.logging.log4j.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.ObjectInputFilter;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PointRepository pointRepository;
    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private UserAchievementRepository userAchievementRepository;
    @Autowired
    private UserProgressRepository userProgressRepository;

    public String uploadUserAvatar(String token, MultipartFile file) throws IOException {
        // Lấy thông tin người dùng từ UserRepository
        String username = jwtService.getUsername(token);
        Optional<Users> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            return null;
        }

        Users user = optionalUser.get();
        // Lấy URL avatar hiện tại của người dùng
        String currentAvatarUrl = user.getAvatarUrl();

        // Tải lên avatar mới và cập nhật URL avatar cho người dùng
        String newAvatarUrl = cloudinaryService.userUploadAvatar(file, currentAvatarUrl);
        user.setAvatarUrl(newAvatarUrl);
        userRepository.save(user);

        return newAvatarUrl;
    }
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }
    @Transactional
    public Users getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(()
                -> new IllegalArgumentException("User not found. Please check your username."));
    }
    @Transactional
    public void deleteUserById(Long userId) {
        // Xóa tất cả các thực thể liên quan đến người dùng trước khi xóa người dùng
        pointRepository.deleteByUserId(userId);
        resultRepository.deleteByUserId(userId);
        userAchievementRepository.deleteByUserId(userId);
        userProgressRepository.deleteByUserId(userId);

        // Xóa người dùng
        userRepository.deleteById(userId);
    }

}
