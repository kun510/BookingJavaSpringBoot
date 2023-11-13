package com.example.doancuoiky.hostel.repository;


import com.example.doancuoiky.hostel.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {
    boolean existsByPhone(String phone);
     Users findByPhone(String phone);
    @Query("SELECT u FROM Users u where u.confirmation_status = 'wait for confirmation' ")
    List<Users> allUsersWait();
    @Query("SELECT u FROM Users u where u.role = '3' ")
    List<Users> allUsers();
    @Query("SELECT u FROM Users u where u.role = '2' ")
    List<Users> allHost();
    @Query("SELECT u FROM Users u WHERE u.id = :id AND u.role = '1'")
    Users findByIdAndRoleEqualsOne(@Param("id") Long id);
    @Query("SELECT u FROM Users u WHERE u.id = :id AND u.role = '2'")
    Users checkHost(@Param("id") Long id);
    @Transactional
    @Modifying
    @Query("UPDATE Users r SET r.confirmation_status = 'confirm' WHERE r.id = :hostId")
    void updateUserStatusById(@Param("hostId") long hostId);

    @Transactional
    @Modifying
    @Query("UPDATE Users r SET r.token_device = :token where r.id = :userID")
    void addToken(@Param("token") String token ,@Param("userID") long userID);

    @Query("SELECT u.id FROM Users u WHERE  u.role = '1'")
    long checkAdmin();

    @Query("SELECT u FROM Users u WHERE u.id= :id and u.role = '1'")
    Users checkAdmin(@Param("id") Long id);

}
