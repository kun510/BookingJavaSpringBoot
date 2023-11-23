package com.example.doancuoiky.hostel.repository;


import com.example.doancuoiky.hostel.model.Rent;
import com.example.doancuoiky.hostel.model.Report;
import com.example.doancuoiky.hostel.model.Room;
import com.example.doancuoiky.hostel.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Report findByUser(Users user);

    @Query("SELECT COUNT(r) FROM Report r WHERE r.room.id = :roomId and r.user.id = :userId")
    Integer findUsersInReport(@Param("roomId") long roomId, @Param("userId") long userId);

    @Transactional
    @Modifying
    @Query("UPDATE Report r SET r.times_report = r.times_report + 1 WHERE r.room.id = :roomId")
    void updateNumberReport(@Param("roomId") long roomId);
    @Query("SELECT DISTINCT count (r.room) FROM Report r WHERE r.room.id = :roomId ")
    int checkRoom(@Param("roomId") Long roomId);

    @Query("select count(r) from Report r")
    int countReport();

    @Query("SELECT DISTINCT r FROM Report r WHERE r.times_report = 1 AND NOT EXISTS (SELECT 1 FROM Report rr WHERE rr.room.id = r.room.id AND rr.times_report > 1)")
    List<Report> ListReportTopTime1();
    @Query("SELECT DISTINCT r FROM Report r WHERE r.times_report = 2 AND NOT EXISTS (SELECT 1 FROM Report rr WHERE rr.room.id = r.room.id AND  rr.times_report > 2)")
    List<Report> ListReportTopTime2();
    @Query("SELECT DISTINCT r FROM Report r WHERE r.times_report = 3 AND NOT EXISTS (SELECT 1 FROM Report rr WHERE rr.room.id = r.room.id AND  rr.times_report > 3)")
    List<Report> ListReportTopTime3();

}
