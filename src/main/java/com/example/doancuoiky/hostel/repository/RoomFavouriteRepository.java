package com.example.doancuoiky.hostel.repository;


import com.example.doancuoiky.hostel.model.RoomFavourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomFavouriteRepository extends JpaRepository<RoomFavourite, Long> {
    @Query("SELECT r  FROM RoomFavourite r where r.user.id = :userID ")
    List<RoomFavourite> allRoomFavourite(@Param("userID") long userID);

    @Query("SELECT r FROM RoomFavourite r WHERE r.id = :idRoomFavourite AND r.user.id = :idUser")
    List<RoomFavourite> checkRoomFavourite(@Param("idRoomFavourite") long idRoomFavourite, @Param("idUser") long idUser);


}
