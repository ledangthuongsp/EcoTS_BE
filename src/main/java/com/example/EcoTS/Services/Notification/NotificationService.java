package com.example.EcoTS.Services.Notification;

import com.example.EcoTS.Models.Locations;
import com.example.EcoTS.Models.ReceiveHistory;
import com.example.EcoTS.Repositories.LocationRepository;
import com.example.EcoTS.Repositories.ReceiveHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {
    @Autowired
    private ReceiveHistoryRepository receiveHistoryRepository;
    @Autowired
    private LocationRepository locationRepository;

    @Transactional
    public void createNotification(Long userId, double points, Long employeeId) {
        Locations location = locationRepository.findByEmployeeId(employeeId).orElseThrow();
        ReceiveHistory receiveHistory = new ReceiveHistory();
        receiveHistory.setUserId(userId);
        receiveHistory.setPoints(points);
        receiveHistory.setExchangePointLocation(location.getTypeOfLocation());
        receiveHistoryRepository.save(receiveHistory);
    }
}
