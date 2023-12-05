package com.example.doancuoiky.hostel.service;

import com.example.doancuoiky.hostel.model.Boarding_host;
import com.example.doancuoiky.hostel.model.Report;
import com.example.doancuoiky.hostel.model.Room;
import com.example.doancuoiky.hostel.model.Users;
import com.example.doancuoiky.hostel.request.AddBoarding;
import com.example.doancuoiky.hostel.request.AddRoom;
import com.example.doancuoiky.hostel.response.ResponseAll;
import com.example.doancuoiky.hostel.response.ResponseOtp;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IadminService {
    List<Users> user();
    List<Users> host();
    List<Users> WaitHost();
    List<Boarding_host> getAllBoardingHostelWaiting();
    ResponseAll UpdateBoardingStatus(long AdminId,long boardingId);
    ResponseAll UpdateHostStatus(long AdminId,long UserId);
    int countUser();
    int countHost();
    int countRoom();
    int countReport();
    List<Report> ListReportTopTime1();
    List<Report> ListReportTopTime2();
    List<Report> ListReportTopTime3();
    ResponseAll Ban(long AdminId,long HostId);
    ResponseAll unBan(long AdminId,long HostId);
    List<Users> ListBan();
    ResponseAll UpdateCancelBoardingStatus(long AdminId,long boardingId);
    ResponseAll UpdateCancelHostStatus(long AdminId,long UserId);
    ResponseOtp sendMailChangePassword(String to);

}
