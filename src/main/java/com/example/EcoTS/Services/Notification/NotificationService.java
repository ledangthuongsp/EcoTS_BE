package com.example.EcoTS.Services.Notification;

import com.example.EcoTS.Models.Locations;
import com.example.EcoTS.Models.ReceiveHistory;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.LocationRepository;
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
    @Transactional
    public void createNotification(Long userId, double points, Long employeeId) {
        Locations location = locationRepository.findByEmployeeId(employeeId).orElseThrow();
        ReceiveHistory receiveHistory = new ReceiveHistory();
        receiveHistory.setUserId(userId);
        receiveHistory.setPoints(points);
        receiveHistory.setExchangePointLocation(location.getTypeOfLocation());
        receiveHistoryRepository.save(receiveHistory);
    }
    public void notifyAllUsers(String message) {
        List<Users> users = userRepository.findAll();
        for (Users user : users) {
            // Here you can implement your logic to send notifications
            // For example, sending an email, an in-app notification, etc.
            System.out.println("Notifying user: " + user.getUsername() + " - " + message);
        }
    }
}
