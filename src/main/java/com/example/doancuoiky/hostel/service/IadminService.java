package com.example.doancuoiky.hostel.service;

import com.example.doancuoiky.hostel.model.Boarding_host;
import com.example.doancuoiky.hostel.model.Room;
import com.example.doancuoiky.hostel.model.Users;
import com.example.doancuoiky.hostel.request.AddBoarding;
import com.example.doancuoiky.hostel.request.AddRoom;
import com.example.doancuoiky.hostel.response.ResponseAll;

import java.util.List;

public interface IadminService {
    List<Users> user(long idAdmin);
    List<Users> host(long idAdmin);
    List<Users> WaitHost(long idAdmin);
    List<Boarding_host> getAllBoardingHostelWaiting();
    ResponseAll UpdateBoardingStatus(long AdminId,long boardingId);
    ResponseAll UpdateHostStatus(long AdminId,long UserId);
}
