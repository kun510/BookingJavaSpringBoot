package com.example.doancuoiky.hostel.repository;

import com.example.doancuoiky.hostel.model.ImgRoom;
import com.example.doancuoiky.hostel.model.NotificationApp;
import com.example.doancuoiky.hostel.model.TotalBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
@Repository
public interface ListImgRepository extends JpaRepository<ImgRoom, Long>  {
    @Transactional
    @Modifying
    @Query("DELETE FROM ImgRoom i WHERE i.room.id = :roomId")
    void deleteByRoomId(@Param("roomId") Long roomId);

    @Query("SELECT i  FROM ImgRoom i where i.room.id = :roomIdD ")
    List<ImgRoom> allImgInRoom(@Param("roomIdD") long roomIdD);
}
