package com.example.doancuoiky.hostel.controller;

import com.example.doancuoiky.hostel.model.Rent;
import com.example.doancuoiky.hostel.model.Room;
import com.example.doancuoiky.hostel.model.TotalBill;
import com.example.doancuoiky.hostel.model.Users;
import com.example.doancuoiky.hostel.request.AddRoom;
import com.example.doancuoiky.hostel.request.BillTotal;
import com.example.doancuoiky.hostel.request.RegisterRq;
import com.example.doancuoiky.hostel.request.UpdateUsers;
import com.example.doancuoiky.hostel.service.IhostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/host")
public class HostController {
    @Autowired
    private IhostService ihostService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRq users) {
        ResponseEntity<String> notification;
        try {
            if (ihostService.isUserPhoneExists(users.getPhone())) {
                return ResponseEntity.badRequest().body("User with the provided phone already exists.");
            } else {
                Users register = ihostService.register(users);
                if (register != null && register.getId() > 0) {
                    notification = ResponseEntity.status(HttpStatus.CREATED).body("Register successfully");
                } else {
                    notification = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register");
                }
            }
        } catch (Exception e) {
            notification = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Register Fail because of DB error");
        }
        return notification;
    }
    @PostMapping("/addroom")
    public ResponseEntity<String> addRoom(@RequestBody AddRoom addRoomRequest, @RequestParam int id_host) {
        Room addedRoom = ihostService.addRoom(addRoomRequest, id_host);
        if (addedRoom != null) {
            return ResponseEntity.ok("ok add room.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail add room");
        }
    }
    @PostMapping("/bill")
    public ResponseEntity<?> bill(@RequestBody BillTotal totalBill, @RequestParam long idRent) {
        try {
            TotalBill totalBillAdd = ihostService.Bill(totalBill, idRent);
            if (totalBillAdd != null) {
                return ResponseEntity.ok("Bill added successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add bill.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding the bill: " + e.getMessage());
        }
    }
    @DeleteMapping("/deleteroom")
    public String DeleteRoom( @RequestParam long idRoom) {
        String result = ihostService.DeleteRoom(idRoom);
        return result;
    }

    @PutMapping("/updateRoom")
    public String updateRoom(@RequestParam long id, @RequestBody AddRoom addRoom){
        return ihostService.UpdateRoom(id, addRoom);
    }
    @GetMapping("/getRents")
    public Object  getRentByHostId(@RequestParam long hostId) {
        List<Rent> rentList = ihostService.getAllRentConfirm(hostId);
        if (!rentList.isEmpty()) {
            return rentList;
        } else {
            return "No List Rent have id_host = "+ hostId;
        }
    }
    @PutMapping("/addUserInRoom")
    public ResponseEntity<?> updateRentStatus(@RequestParam("roomId") long roomId) {
        String result = ihostService.AddUserInRoom(roomId);
        if ("Confirmed successfully".equals(result)) {
            return ResponseEntity.ok("Status update successful");
        } else {
            return ResponseEntity.badRequest().body("Status update failed");
        }
    }
    @GetMapping("/usersByHost")
    public ResponseEntity<List<Users>> getUsersByHost(@RequestParam long hostId) {
        List<Users> users = ihostService.getAllUser(hostId);

        if (!users.isEmpty()) {
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/RoomByHost")
    public ResponseEntity<List<Room>> getRoomByHost(@RequestParam long hostId) {
        List<Room> room = ihostService.getAllRoomByHost(hostId);

        if (!room.isEmpty()) {
            return ResponseEntity.ok(room);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
