package com.example.doancuoiky.hostel.repository;

import com.example.doancuoiky.hostel.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ReviewReponsitory extends JpaRepository<Review, Long> {
    boolean existsByUserAndRoom(Users user, Room room);
    boolean existsByUserAndBoarding(Users user, Boarding_host Boarding);
    List<Review> findByRoom(Room room);
    List<Review> findByBoarding(Boarding_host boardingHost);
//    @Modifying
//    @Transactional
//    @Query("UPDATE Review r SET r.numberOfStars = :numberOfStars WHERE r.id = :reviewId")
//    void updateNumberOfStars(Long reviewId, String numberOfStars);
}