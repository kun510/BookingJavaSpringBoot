package com.example.doancuoiky.hostel.repository;

import com.example.doancuoiky.hostel.model.Boarding_host;
import com.example.doancuoiky.hostel.model.ImgRoom;
import com.example.doancuoiky.hostel.model.Room;
import com.example.doancuoiky.hostel.model.Users;
import com.example.doancuoiky.hostel.request.areaRq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface BoardingRepository extends JpaRepository<Boarding_host, Long> {
    @Query("SELECT r FROM Boarding_host r where r.status != 'confirm' and r.user.id = :hostId ")
    List<Boarding_host> allRoomsOk(@Param("hostId") long hostId);
    @Query("SELECT r,r.user FROM Boarding_host r where r.status = 'Waiting Confirm'")
    List<Boarding_host> allRoomsWaiting();
    @Transactional
    @Modifying
    @Query("UPDATE Boarding_host r SET r.status = 'confirm' WHERE r.id = :boardingId")
    void updateBoardingStatusById(@Param("boardingId") long boardingId);
    @Transactional
    @Modifying
    @Query("UPDATE Boarding_host r SET r.numberRoom = r.numberRoom + 1 WHERE r.id = :boardingId")
    void updateBoardingNumber(@Param("boardingId") long boardingId);
    boolean existsByStatusAndId(String status, Long id);
    @Query("SELECT r FROM Boarding_host r where r.id = :Id and r.user.id = :userId ")
    Optional<Boarding_host> findIdAndHost  (@Param("Id") long Id,@Param("userId") long userId);
    @Query("SELECT r FROM Boarding_host r where r.status = 'confirm' and r.area = :area")
    List<Boarding_host> allRooms(@Param("area") String area);
    @Query("SELECT r FROM Boarding_host r where r.status = 'confirm' ")
    List<Boarding_host> allBoarding();
    @Query("SELECT r FROM Boarding_host r where  r.user.id = :hostId")
    List<Boarding_host> allBoardingByHost(@Param("hostId") long hostId);
    @Query("SELECT r.user FROM Boarding_host r WHERE r.id = :Id ")
    Users findHostById(@Param("Id") long Id);
    @Transactional
    @Modifying
    @Query("UPDATE Boarding_host r SET r.status = 'cancel' WHERE r.id = :boardingId")
    void updateCancelBoardingStatusById(@Param("boardingId") long boardingId);

}
