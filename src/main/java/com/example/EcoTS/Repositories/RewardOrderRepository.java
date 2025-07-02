package com.example.EcoTS.Repositories;

import com.example.EcoTS.Models.Locations;
import com.example.EcoTS.Models.Reward.RewardOrder;
import com.example.EcoTS.Models.Users;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Hidden
@Repository
public interface RewardOrderRepository extends JpaRepository<RewardOrder, Long> {
    List<RewardOrder> findByUserOrderByCreatedAtDesc(Users user);
    List<RewardOrder> findByLocationOrderByCreatedAtDesc(Locations location);
    @Query("SELECT o FROM RewardOrder o WHERE o.status = 'PENDING' AND o.createdAt <= :threshold")
    List<RewardOrder> findExpiredPendingOrders(@Param("threshold") Timestamp threshold);

}
