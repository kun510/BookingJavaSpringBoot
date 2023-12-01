package com.example.doancuoiky.hostel.repository;

import com.example.doancuoiky.hostel.model.Boarding_host;
import com.example.doancuoiky.hostel.model.Review;
import com.example.doancuoiky.hostel.model.ReviewBoarding;
import com.example.doancuoiky.hostel.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewBoardingReponsitory extends JpaRepository<ReviewBoarding, Long> {
    boolean existsByUserAndBoarding(Users user, Boarding_host Boarding);
    List<Review> findByBoarding(Boarding_host boardingHost);
}
