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
import java.util.Optional;

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
    @Transactional
    @Modifying
    @Query("UPDATE Rent r SET r.status = 'Confirmed successfully' WHERE r.id = :rentId")
    Integer updateRentStatusByRoomIdMobile(@Param("rentId") long rentId);

    @Transactional
    @Modifying
    @Query("UPDATE Rent r SET r.status = 'Cancel' WHERE r.id = :rentId")
    Integer cancelRentStatusByRoomIdMobile(@Param("rentId") long rentId);

    @Query("SELECT DISTINCT r FROM Rent r WHERE r.room.user.id = :hostId and r.status = 'Confirmed successfully'")
    List<Rent> findUsersByHostId(@Param("hostId") Long hostId);

    @Query("SELECT DISTINCT r.user,r.room FROM Rent r WHERE r.room.id = :roomId and r.user.id = :userId")
    Optional<Rent> findUsersInRent(@Param("roomId") Long roomId, @Param("userId") Long userId);
    @Query("SELECT r.room.boardingHostel.id FROM Rent r WHERE r.room.id = :roomId")
    int findBoardingIdByRoomId(@Param("roomId") Long roomId);
    @Query("SELECT r FROM Rent r WHERE r.status = 'Confirmed successfully' and r.user.id = :userId AND r.room.id = :roomId")
    List<Rent> checkUserBelongsToBoarding(@Param("userId") Long userId, @Param("roomId") Long roomId);

    @Query("SELECT r FROM Rent r WHERE r.status = 'Confirmed successfully' and r.user.id = :userId AND r.room.boardingHostel.id = :boardingId")
    List<Rent> checkUserBelongsTo(@Param("userId") Long userId, @Param("boardingId") Long boardingId);

    @Query("SELECT r.peopleInRoom FROM Rent r where r.room.id = :idRoom")
    int peopleInRoom(@Param("idRoom") long idRoom);

    @Query("SELECT count(r.room.user.id) FROM Rent r where r.room.user.id = :idUser")
    int peopleRent(@Param("idUser") long idUser);

    @Query("SELECT r.room.id FROM Rent r where r.id = :idRent")
    long idRoomByRent(@Param("idRent") long idRent);

    @Query("SELECT r.user FROM Rent r where r.room.id = :idRoom")
    Users idUserByRent(@Param("idRoom") long idRoom);

    @Query("SELECT r.room.user FROM Rent r where r.room.id = :idRoom")
    Users idHostByRent(@Param("idRoom") long idRoom);

    @Query("SELECT r FROM Rent r where r.user.id = :idUser and r.status = 'Confirmed successfully'")
    List<Rent> MyRoomByIdUser(@Param("idUser") long idUser);

    @Query("select r.user.token_device from Rent r  where r.room.user.id = :hostId ")
    List<String> tokenUser(@Param("hostId") long hostId);

    @Query("SELECT r FROM Rent r where r.room.user.id = :idHost and r.status = 'Confirmed successfully'")
    List<Rent> getUserInRent(@Param("idHost") long idHost);
}
