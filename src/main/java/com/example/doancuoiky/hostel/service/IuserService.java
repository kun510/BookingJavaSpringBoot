package com.example.doancuoiky.hostel.service;

import com.example.doancuoiky.hostel.model.*;
import com.example.doancuoiky.hostel.request.*;
import com.example.doancuoiky.hostel.response.ResponseAll;
import com.example.doancuoiky.hostel.response.ResponseToken;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IuserService {

    //ham them user
    Users register(RegisterRq Users);
    ResponseAll changePassword(long id,String password);
    Users getUserByUsername(String phone);
     // check username
    Users login(String phone, String password);
    //check phone
      boolean isUserPhoneExists(String phone) ;
    //chinh sua
     Users updateuser(long id, UpdateUsers Users,MultipartFile avt);

    //xoa user
      boolean deleteuser(long id);

    boolean deleteRoomFavourite(long idRoomFavourite,long idUser);
    //lay danh sach
     List<Users> getAlluser();
    List<Users> getUserCurrent(long idUser);
     //lay danh sach tro
    List<Room> getAllRoom();
    List<Room> getAllRoomHot();
    List<Rent> getMyRoom(long idUser);
     //lay hoa don
    List<TotalBill> CheckBill(long idRoom);

    List<ImgRoom> ListImgInRoom(long idRoom);
    List<Boarding_host> allList();

    //lay ra 1 user
     Users getoneuser(long id);
    Rent Rent(long idRoom, long idUser, int numberUserInRoom);

    ResponseAll Report(ReportRq report, MultipartFile imageFile1, MultipartFile imageFile2, MultipartFile imageFile3, long idRoom, long idUser);

    Review Review(ReviewRq reviewRq,long idRoom,long idUser);

    ReviewBoarding ReviewBoarding(ReviewRq reviewRq, long idBoarding, long idUser);
    List<Room> searchRooms(Integer  price, String area,String people,String type);

    List<TotalBill> getAllBill(long idUser);

    List<Boarding_host> getBoarding(String area);
    List<Room> allRoomByBoarding(long boardingId);

    ResponseAll addTokenDevice(String token,long userID);

    ResponseAll sendNotificationByToken(NotificationMessaging notificationMessaging);

    List<RoomFavourite> listRoomFavourite(long idUser);

    ResponseAll addRoomFavourite(long userId,long roomId);

    List<NotificationApp> listNotificationReceiver(long userId);

    ResponseToken getToken(long idUser);

    List<Users> UserCurrent(long UserId);

    List<String> tokenUser(long hostId);

}
