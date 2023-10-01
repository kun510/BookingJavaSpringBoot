package com.example.doancuoiky.hostel.service;

import com.example.doancuoiky.hostel.model.Rent;
import com.example.doancuoiky.hostel.model.Room;
import com.example.doancuoiky.hostel.model.TotalBill;
import com.example.doancuoiky.hostel.model.Users;
import com.example.doancuoiky.hostel.request.AddRoom;
import com.example.doancuoiky.hostel.request.BillTotal;
import com.example.doancuoiky.hostel.request.RegisterRq;
import com.example.doancuoiky.hostel.request.UpdateUsers;

import java.util.List;


public interface IhostService {
    Users register(RegisterRq Users);
    boolean isUserPhoneExists(String phone) ;
    Room addRoom(AddRoom addRoom,long host);
    TotalBill Bill(BillTotal totalBill, long idRent);
    String UpdateRoom(long id, AddRoom UpdateRoom);
    String DeleteRoom(long id);
    String AddUserInRoom(long roomId);
    List<Rent> getAllRentConfirm(long hostId);
    List<Users> getAllUser(long hostId);
    List<Room> getAllRoomByHost(long hostId);
}
