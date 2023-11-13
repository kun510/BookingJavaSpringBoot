package com.example.doancuoiky.hostel.service;

import com.example.doancuoiky.hostel.model.*;
import com.example.doancuoiky.hostel.request.AddRoom;
import com.example.doancuoiky.hostel.request.BillTotal;
import com.example.doancuoiky.hostel.request.RegisterRq;
import com.example.doancuoiky.hostel.request.UpdateUsers;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface IhostService {
    Users register(RegisterRq Users);
    boolean isUserPhoneExists(String phone) ;
   // Room addRoom(AddRoom addRoom, long host, MultipartFile imageFile);
   Room addRoom(AddRoom addRoom, long hostId, MultipartFile imageFile) throws IOException;
    TotalBill Bill(BillTotal totalBill, long idRent);
    String UpdateRoom(long id, AddRoom UpdateRoom);
    String DeleteRoom(long id);
    String AddUserInRoom(long roomId);
    List<Rent> getAllRentConfirm(long hostId);
    List<Users> getAllUser(long hostId);
    List<Room> getAllRoomByHost(long hostId);

    String uploadImageToFileSystem(AddRoom addRoom,MultipartFile file) throws IOException;
    List<byte[]> downloadImagesFromFileSystem(String fileName) throws IOException;
    MediaType determineMediaType(String fileName);

    //test
    Map uploadImage(MultipartFile file) throws IOException;
}
