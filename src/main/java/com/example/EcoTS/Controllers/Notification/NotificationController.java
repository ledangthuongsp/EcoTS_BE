package com.example.EcoTS.Controllers.Notification;

import com.example.EcoTS.Models.DonationHistory;
import com.example.EcoTS.Models.Notifications;
import com.example.EcoTS.Models.ReceiveHistory;
import com.example.EcoTS.Repositories.DonationHistoryRepository;
import com.example.EcoTS.Repositories.DonationRepository;
import com.example.EcoTS.Repositories.ReceiveHistoryRepository;
import com.example.EcoTS.Services.DonationService.DonationHistoryService;
import com.example.EcoTS.Services.DonationService.DonationService;
import com.example.EcoTS.Services.Notification.NotificationService;
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
    @Autowired
    private DonationHistoryService donationHistoryService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReceiveHistory>> getAllNotificationsByUserId(@PathVariable Long userId) {
        List<ReceiveHistory> notifications = receiveHistoryRepository.findAllByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/donation-history/{userId}")
    public List<DonationHistory> getAllDonationHistoryByUserId(@PathVariable Long userId) {
        return donationHistoryService.getDonationHistoryByUserId(userId);
    }

    @GetMapping("/notifications")
    public List<Notifications> getAllNotifications() {
        return notificationService.getAllNotifications();
    }
}
