package com.example.doancuoiky.hostel.repository;

import com.example.doancuoiky.hostel.model.Rent;
import com.example.doancuoiky.hostel.model.Room;
import com.example.doancuoiky.hostel.model.TotalBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<TotalBill, Long> {
    @Query("SELECT b FROM TotalBill b")
    List<TotalBill> listTotal();
    Optional<TotalBill> findByRentAndMonth(Rent rent, String month);
    @Query("SELECT tb FROM TotalBill tb JOIN tb.rent r WHERE r.user.id = :userId")
    List<TotalBill> findTotalBillsByUserId(@Param("userId") long userId);
}
