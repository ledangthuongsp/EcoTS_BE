package com.example.EcoTS.Services.Notification;

import com.example.EcoTS.Enum.Roles;
import com.example.EcoTS.Models.Locations;
import com.example.EcoTS.Models.Notifications;
import com.example.EcoTS.Models.ReceiveHistory;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.LocationRepository;
import com.example.EcoTS.Repositories.NotificationRepository;
import com.example.EcoTS.Repositories.ReceiveHistoryRepository;
import com.example.EcoTS.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private ReceiveHistoryRepository receiveHistoryRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Transactional
    public void createNotification(Long userId, double points, Long employeeId) {
        Users employee = userRepository.findById(employeeId).orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        if (!Roles.EMPLOYEE.name().equals(employee.getRole())) {
            throw new IllegalArgumentException("Only employees can create notifications");
        }

        Locations location = locationRepository.findByEmployeeId(employeeId).orElseThrow();
        ReceiveHistory receiveHistory = new ReceiveHistory();
        receiveHistory.setUserId(userId);
        receiveHistory.setPoints(points);
        receiveHistory.setExchangePointLocation(location.getTypeOfLocation());
        receiveHistoryRepository.save(receiveHistory);
    }

    public void notifyAllUsers(String title, String description, String username) {
        Users user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!Roles.EMPLOYEE.name().equals(user.getRole())) {
            throw new IllegalArgumentException("Only employees can notify all users");
        }

        List<Users> users = userRepository.findAll();
        Notifications notification = Notifications.builder()
                .title(title)
                .description(description)
                .build();
        notificationRepository.save(notification);

        for (Users u : users) {
            // Here you can implement your logic to send notifications
            // For example, sending an email, an in-app notification, etc.
            System.out.println("Notifying user: " + u.getUsername() + " - " + title + ": " + description);
        }
    }
    public List<Notifications> getAllNotifications() {
        return notificationRepository.findAll();
    }
}
