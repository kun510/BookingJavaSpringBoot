package com.example.doancuoiky.hostel.service;

import com.example.doancuoiky.hostel.model.*;
import com.example.doancuoiky.hostel.request.*;
import com.example.doancuoiky.hostel.response.ResponseAll;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface IhostService {
    Users register(RegisterRq Users);
    boolean isUserPhoneExists(String phone) ;
   // Room addRoom(AddRoom addRoom, long host, MultipartFile imageFile);
   ResponseAll addRoom(AddRoom addRoom, long boardingId, long hostId, MultipartFile imageFile) throws IOException;
    ResponseAll addBoarding_hostel(AddBoarding addBoarding, long hostId, MultipartFile imageFile)throws IOException;
    TotalBill Bill(BillTotal totalBill, long idRent,long hostId);
    String UpdateRoom(long id, AddRoom UpdateRoom);
    String DeleteRoom(Long id);
    String AddUserInRoom(long roomId);
    String AddUserInRoomMobile(long roomId,long rentId);
    String CancelUserInRoomMobile(long roomId,long rentId);
    ResponseAll RentOver(long roomId,long rent);
    List<Rent> getAllRentConfirm(long hostId);
    List<Rent> getAllUser(long hostId);
    List<Room> getAllRoomByHost(long hostId);
    List<Room> AllRoom(long hostId,long boardingId);
    List<ListandCoutRoom>  RoomEmptyByBoarding(long HostId);
    List<Boarding_host> getAllBoardingHostelConfirm(long hostId);
    List<Boarding_host> getAllBoardingByUser(long hostId);
    int countRoomEmpty(long BoardingId,long HostId);
    int countRoomEmptyReal(long HostId);
    int contRoom(long HostId);
   int contRent(long HostId);
   ResponseAll Help(HelpRq helpRq,long idHost);

   List<Rent> getUserInRent(long HostId);
    //load img
    Map uploadImage(MultipartFile file) throws IOException;
     ImgRoom addImgRoom(long idRoom, long hostId,MultipartFile imageFile1,MultipartFile imageFile2,MultipartFile
            imageFile3,MultipartFile imageFile4,MultipartFile imageFile5) throws IOException;

}
