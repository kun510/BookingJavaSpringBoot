package com.example.doancuoiky.hostel.repository;

import com.example.doancuoiky.hostel.model.Help;
import com.example.doancuoiky.hostel.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HelpRepository extends JpaRepository<Help,Long> {
    @Query("SELECT h.user FROM Help h WHERE h.user.id = :idHost")
    Users idHostByHelp(@Param("idHost") long idHost);
}
