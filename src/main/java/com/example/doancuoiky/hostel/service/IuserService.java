package com.example.doancuoiky.hostel.service;

import com.example.doancuoiky.hostel.model.*;
import com.example.doancuoiky.hostel.request.RegisterRq;
import com.example.doancuoiky.hostel.request.ReviewRq;
import com.example.doancuoiky.hostel.request.UpdateUsers;
import com.example.doancuoiky.hostel.request.ReportRq;

import java.util.List;

public interface IuserService {

    //ham them user
    Users register(RegisterRq Users);
    Users getUserByUsername(String phone);
     // check username
    Users login(String phone, String password);
    //check phone
      boolean isUserPhoneExists(String phone) ;
    //chinh sua
     Users updateuser(long id, UpdateUsers Users);

    //xoa user
      boolean deleteuser(long id);

    //lay danh sach
     List<Users> getAlluser();

     //lay danh sach tro
    List<Room> getAllRoom();
    List<Room> getAllRoomHot();
     //lay hoa don
    List<TotalBill> CheckBill(long idRoom);

    //lay ra 1 user
     Users getoneuser(long id);
    Rent Rent(long idRoom, long idUser);

    Report Report(ReportRq report, long idRoom, long idUser);

    Review Review(ReviewRq reviewRq,long idRoom,long idUser);

    List<Room> searchRooms(String address, String price, String area,String people,String type);

    List<TotalBill> getAllBill(long idUser);
}
