package com.example.doancuoiky.hostel.service;

import com.example.doancuoiky.hostel.model.Boarding_host;
import com.example.doancuoiky.hostel.model.NotificationApp;
import com.example.doancuoiky.hostel.model.Users;
import com.example.doancuoiky.hostel.repository.BoardingRepository;
import com.example.doancuoiky.hostel.repository.NotificationRepository;
import com.example.doancuoiky.hostel.repository.UserRepository;
import com.example.doancuoiky.hostel.response.ResponseAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImplement implements IadminService{

    @Autowired
    private BoardingRepository boardingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public List<Users> user(long idAdmin) {
        boolean isAdmin = checkAdmin(idAdmin);
        if (isAdmin) {
            return userRepository.allUsers();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Users> host(long idAdmin) {
        boolean isAdmin = checkAdmin(idAdmin);
        if (isAdmin) {
            return userRepository.allHost();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Users> WaitHost(long idAdmin) {
        boolean isAdmin = checkAdmin(idAdmin);
        if (isAdmin) {
            return userRepository.allUsersWait();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Boarding_host> getAllBoardingHostelWaiting() {
        return boardingRepository.allRoomsWaiting();
    }

    @Override
    public ResponseAll UpdateBoardingStatus(long AdminId, long boardingId) {
            boolean isAdmin = checkAdmin(AdminId);
            if (isAdmin){
                Boarding_host boardingHostel = getBoardingIfExists(boardingId);
                boardingRepository.updateBoardingStatusById(boardingId);
               Users admin = userRepository.checkAdmin(AdminId);
               Users host = boardingRepository.findHostById(boardingId);
                NotificationApp notificationApp = new NotificationApp();
                notificationApp.setUser_id_sender(admin);
                notificationApp.setUser_id_receiver(host);
                notificationApp.setContent("Your Boarding confirmed by Admin");
                Date currentTime = new Date();
                notificationApp.setTime(new Date(currentTime.getTime()));
                notificationRepository.save(notificationApp);
                return new ResponseAll(true, "Boarding status updated successfully");
            }else {
                return new ResponseAll(false,"User is not a Admin.");
            }
    }

    @Override
    public ResponseAll UpdateHostStatus(long AdminId, long UserId) {
        boolean isAdmin = checkAdmin(AdminId);
        boolean isHost = checkHost(UserId);
        if (isAdmin){
            if (isHost){
                Users admin = userRepository.checkAdmin(AdminId);
                Users host = userRepository.checkHost(UserId);
                NotificationApp notificationApp = new NotificationApp();
                notificationApp.setUser_id_sender(admin);
                notificationApp.setUser_id_receiver(host);
                notificationApp.setContent("Your account confirmed by Admin");
                userRepository.updateUserStatusById(UserId);
                Date currentTime = new Date();
                notificationApp.setTime(new Date(currentTime.getTime()));
                notificationRepository.save(notificationApp);
                return new ResponseAll(true,"Update Successfully");
            }else {
                return new ResponseAll(false,"not Found");
            }

        }else {
            return new ResponseAll(false,"Cannot update status because the user is not a Admin.");
        }
    }

    private Boarding_host getBoardingIfExists(long boardingId) {
        Optional<Boarding_host> boardingHost = boardingRepository.findById(boardingId);
        if (!boardingHost.isPresent()) {
            throw new RuntimeException("Boarding Host Not Found");
        }
        return boardingHost.get();
    }

    boolean checkAdmin(long AdminId) {
        Users users = userRepository.findByIdAndRoleEqualsOne(AdminId);
        return users != null;
    }
    boolean checkHost(long Host){
        Users users = userRepository.checkHost(Host);
        return users != null;
    }

}
