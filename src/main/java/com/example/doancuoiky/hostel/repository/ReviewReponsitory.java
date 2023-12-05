package com.example.doancuoiky.hostel.repository;

import com.example.doancuoiky.hostel.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ReviewReponsitory extends JpaRepository<Review, Long> {
    boolean existsByUserAndRoom(Users user, Room room);

    List<Review> findByRoom(Room room);
    @Query("SELECT r FROM Review r where r.room.boardingHostel.id = :boardingId ")
    List<Review> allRoomByBoarding(@Param("boardingId") Boarding_host boardingId);

    @Query("SELECT r FROM Review r where r.room.id = :roomId ")
    List<Review> allReviewByRoom(@Param("roomId") long roomId);
    @Query("SELECT r FROM Review r where r.room.user.id = :hostId ")
    List<Review> allReviewByHost(@Param("hostId") long hostId);
//    @Modifying
//    @Transactional
//    @Query("UPDATE Review r SET r.numberOfStars = :numberOfStars WHERE r.id = :reviewId")
//    void updateNumberOfStars(Long reviewId, String numberOfStars);
}
