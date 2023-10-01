package com.example.doancuoiky.hostel.repository;


import com.example.doancuoiky.hostel.model.Report;
import com.example.doancuoiky.hostel.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Report findByUser(Users user);
}
