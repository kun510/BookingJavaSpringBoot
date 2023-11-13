package com.example.doancuoiky.hostel.repository;

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
public interface RoomRepository extends JpaRepository<Room,Long> {
    @Query("SELECT r FROM Room r where r.status != 'hired'")
    List<Room> allRooms();

    Room findRoomById(long id);
//    @Query("SELECT r FROM Room r WHERE r.address LIKE %:address%")
//    List<Room> findByAddressContaining(@Param("address") String address);

    @Query("SELECT r FROM Room r WHERE r.price = :price")
    List<Room> findByPrice(@Param("price") String price);

//    @Query("SELECT r FROM Room r WHERE r.area = :area")
//    List<Room> findByArea(@Param("area") String area);

    @Query("SELECT r FROM Room r WHERE r.people = :people")
    List<Room> findByPeople(@Param("people") String people);

    @Query("SELECT r FROM Room r WHERE r.type = :type")
    List<Room> findByType(@Param("type") String type);

    @Transactional
    @Modifying
    @Query("UPDATE Room r SET r.status = 'hired' WHERE r.id = :roomId")
    void updateRoomStatusById(@Param("roomId") long roomId);

    @Query("SELECT r FROM Room r where r.status = 'still empty' and r.user.id = :hostId")
    List<Room> allRoomsEmpty(@Param("hostId") long hostId);

    @Query("SELECT r FROM Room r where r.numberOfStars >= 4 ")
    List<Room> allRoomsHot();

    @Query("SELECT r FROM Room r where r.status = 'empty room' and r.boardingHostel.id = :boardingId and r.boardingHostel.status = 'confirm'")
    List<Room> allRoomByBoarding(@Param("boardingId") long boardingId);

    @Query("SELECT r.user FROM Room r where r.id = :idRoom ")
    Users findHostByIdRoom(@Param("idRoom") long idRoom);

    @Query("SELECT r.people FROM Room r where r.id = :idRoom ")
    int numberUserInRoom(@Param("idRoom") long idRoom);

    @Query("SELECT r.WaterBill FROM Room r where r.id = :idRoom")
    int priceWater(@Param("idRoom") long idRoom);
}
