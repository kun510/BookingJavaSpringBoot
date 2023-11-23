package com.example.doancuoiky.hostel.repository;

import com.example.doancuoiky.hostel.model.NotificationApp;
import com.example.doancuoiky.hostel.model.RoomFavourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationApp, Long> {

    @Query("SELECT n  FROM NotificationApp n where n.user_id_receiver.id = :userID order by n.time DESC ")
    List<NotificationApp> allNotificationReceiver(@Param("userID") long userID);
}
