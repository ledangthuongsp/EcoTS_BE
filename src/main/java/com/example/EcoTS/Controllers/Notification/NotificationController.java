package com.example.EcoTS.Controllers.Notification;

import com.example.EcoTS.Models.ReceiveHistory;
import com.example.EcoTS.Repositories.ReceiveHistoryRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin
@Tag(name = "Notification")
public class NotificationController {
    @Autowired
    private ReceiveHistoryRepository receiveHistoryRepository;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReceiveHistory>> getAllNotificationsByUserId(@PathVariable Long userId) {
        List<ReceiveHistory> notifications = receiveHistoryRepository.findAllByUserId(userId);
        return ResponseEntity.ok(notifications);
    }
}
