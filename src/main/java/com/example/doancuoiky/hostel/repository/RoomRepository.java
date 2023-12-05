package com.example.doancuoiky.hostel.repository;

import com.example.doancuoiky.hostel.model.ListandCoutRoom;
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
    @Query("SELECT r FROM Room r where r.status != 'hired' and r.user.confirmation_status != 'ban'")
    List<Room> allRooms();

    Room findRoomById(long id);
//    @Query("SELECT r FROM Room r WHERE r.address LIKE %:address%")
//    List<Room> findByAddressContaining(@Param("address") String address);

    @Query("SELECT r FROM Room r WHERE r.price <= :price")
    List<Room> findByPrice(@Param("price") int price);

    @Query("SELECT r FROM Room r WHERE r.boardingHostel.area = :area")
    List<Room> findByArea(@Param("area") String area);

    @Query("SELECT r FROM Room r WHERE r.people <= :people")
    List<Room> findByPeople(@Param("people") String people);

    @Query("SELECT r FROM Room r WHERE r.type = :type")
    List<Room> findByType(@Param("type") String type);

    @Transactional
    @Modifying
    @Query("UPDATE Room r SET r.status = 'hired' WHERE r.id = :roomId")
    Integer updateRoomStatusById(@Param("roomId") long roomId);
    @Transactional
    @Modifying
    @Query("UPDATE Room r SET r.status = 'empty room' WHERE r.id = :roomId")
    Integer updateRentOver(@Param("roomId") long roomId);

    @Query("SELECT r FROM Room r where r.status = 'empty room' and r.user.id = :hostId")
    List<Room> allRoomsEmpty(@Param("hostId") long hostId);

    @Query("SELECT r FROM Room r where r.user.id = :hostId and r.boardingHostel.id = :boardingId")
    List<Room> allRoomsNe(@Param("hostId") long hostId,@Param("boardingId") long boardingId);
    @Query("SELECT r FROM Room r where r.numberOfStars >= 4 ")
    List<Room> allRoomsHot();

    @Query("SELECT r FROM Room r where r.status = 'empty room' and r.boardingHostel.id = :boardingId and r.boardingHostel.user.confirmation_status = 'confirm' and r.boardingHostel.status = 'confirm'")
    List<Room> allRoomByBoarding(@Param("boardingId") long boardingId);

    @Query("SELECT r.user FROM Room r where r.id = :idRoom ")
    Users findHostByIdRoom(@Param("idRoom") long idRoom);

    @Query("SELECT r.people FROM Room r where r.id = :idRoom ")
    int numberUserInRoom(@Param("idRoom") long idRoom);

    @Query("SELECT r.WaterBill FROM Room r where r.id = :idRoom")
    int priceWater(@Param("idRoom") long idRoom);

    @Query("select count(r) from Room r")
    int countRoom();

    @Query("SELECT count(r) from Room r where r.boardingHostel.id = :BoardingId and r.user.id =:HostId and r.status ='empty room'")
    int countRoomEmpty(@Param("BoardingId") long BoardingId,@Param("HostId") long HostId);

    @Query("SELECT count(r) from Room r where r.user.id =:HostId and r.status ='empty room'")
    int countRoomEmptyReal(@Param("HostId") long HostId);
    @Query("SELECT count(r) from Room r where r.user.id =:HostId")
    int countRoom(@Param("HostId") long HostId);
    @Query("SELECT new com.example.doancuoiky.hostel.model.ListandCoutRoom(r.boardingHostel, COUNT(r.id))  FROM Room r WHERE r.user.id = :hostId AND r.status = 'empty room' GROUP BY r.boardingHostel.id")
    List<ListandCoutRoom> getRoomCountsForHost(long hostId);

}
