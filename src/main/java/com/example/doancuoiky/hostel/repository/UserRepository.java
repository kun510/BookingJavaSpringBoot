package com.example.doancuoiky.hostel.repository;

import com.example.doancuoiky.hostel.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {
    boolean existsByPhone(String phone);
     Users findByPhone(String phone);

}
