package com.example.doancuoiky.hostel.repository;


import com.example.doancuoiky.hostel.model.Rent;
import com.example.doancuoiky.hostel.model.Report;
import com.example.doancuoiky.hostel.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Report findByUser(Users user);
    @Query("SELECT DISTINCT r.user FROM Report r WHERE r.room.id = :roomId and r.user.id = :userId")
    Optional<Report> findUsersInReport(@Param("roomId") Long roomId, @Param("userId") Long userId);

    @Query("SELECT DISTINCT count (r.room) FROM Report r WHERE r.room.id = :roomId ")
    int checkRoom(@Param("roomId") Long roomId);
}
