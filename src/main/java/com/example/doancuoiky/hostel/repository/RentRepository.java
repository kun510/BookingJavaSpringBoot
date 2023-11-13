package com.example.doancuoiky.hostel.repository;

import com.example.doancuoiky.hostel.model.Rent;
import com.example.doancuoiky.hostel.model.Room;
import com.example.doancuoiky.hostel.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface RentRepository extends JpaRepository<Rent, Long> {
    Rent findByUser(Users user);
    Rent findByRoomId(long roomId);
    Rent findByUserId(long userid);
    @Query("SELECT r FROM Rent r WHERE r.room.id IN (SELECT rm.id FROM Room rm WHERE rm.user.id = :hostId AND r.status = 'wait for confirmation')")
    List<Rent> findRentByHostId(@Param("hostId") long hostId);
    @Transactional
    @Modifying
    @Query("UPDATE Rent r SET r.status = 'Confirmed successfully' WHERE r.room.id = :roomId")
    void updateRentStatusByRoomId(@Param("roomId") long roomId);
    @Query("SELECT DISTINCT r.user FROM Rent r WHERE r.room.user.id = :hostId")
    List<Users> findUsersByHostId(@Param("hostId") Long hostId);
}
