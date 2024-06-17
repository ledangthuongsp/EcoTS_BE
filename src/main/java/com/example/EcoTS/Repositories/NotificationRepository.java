package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.Notifications;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Hidden
public interface NotificationRepository extends JpaRepository<Notifications, Long> {
}
