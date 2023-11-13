package com.example.doancuoiky.hostel.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.doancuoiky.hostel.model.*;
import com.example.doancuoiky.hostel.request.AddBoarding;
import com.example.doancuoiky.hostel.request.AddRoom;
import com.example.doancuoiky.hostel.request.BillTotal;
import com.example.doancuoiky.hostel.request.RegisterRq;
import com.example.doancuoiky.hostel.response.Response;
import com.example.doancuoiky.hostel.response.ResponseAll;
import com.example.doancuoiky.hostel.service.IhostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


import static org.hibernate.bytecode.BytecodeLogger.LOGGER;

@RestController
@RequestMapping(path = "/host")
public class HostController {
    @Autowired
    private IhostService ihostService;


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRq users) {
        try {
            if (ihostService.isUserPhoneExists(users.getPhone())) {
                return ResponseEntity.badRequest().body("User with the provided phone already exists.");
            } else {
                Users register = ihostService.register(users);
                if (register != null && register.getId() > 0) {
                    return ResponseEntity.status(HttpStatus.CREATED).body("Register successfully");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register");
                }
            }
        } catch (Exception e) {
            // Log the exception for debugging purposes.
            LOGGER.error("Registration failed due to an exception: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed due to a server error");
        }
    }

    @PostMapping("/addRoom")
    public ResponseEntity<ResponseAll> addRoom(@Valid @ModelAttribute AddRoom addRoomRequest, @RequestParam("boardingId") long boardingId, @RequestParam("hostId") long hostId, @RequestParam("image") MultipartFile imageFile) {
        try {
            ResponseAll addedRoom = ihostService.addRoom(addRoomRequest, boardingId, hostId, imageFile);
            if (addedRoom.isSuccess()) {
                return new ResponseEntity<>(addedRoom, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(addedRoom, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseAll(false, "Failed to add room: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addBoardingHostel")
    public ResponseEntity<ResponseAll> addBoardingHostel(@Valid @ModelAttribute AddBoarding addBoarding, @RequestParam("hostId") long hostId, @RequestParam("image") MultipartFile imageFile) {
        try {
            ResponseAll addedRoom = ihostService.addBoarding_hostel(addBoarding, hostId, imageFile);
            if (addedRoom.isSuccess()) {
                return new ResponseEntity<>(addedRoom, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(addedRoom, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseAll(false, "Failed to add Boarding Hostel: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addBill")
    public ResponseEntity<?> addBill(@Valid @ModelAttribute BillTotal addBill, @RequestParam("rentId") long rentId, @RequestParam("hostId") long hostId) {

            TotalBill addBilliRent = ihostService.Bill(addBill, rentId,hostId);
            if (addBilliRent != null) {
                return new ResponseEntity<>("Room added successfully", HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add room ccc");
            }

    }

    @PostMapping("/addImgRoom")
    public ResponseEntity<ResponseAll> addImgRoom(
            @RequestParam("idRoom") long idRoom,
            @RequestParam("hostId") long hostId,
            @RequestParam(name = "image1", required = false) MultipartFile imageFiles1,
            @RequestParam(name = "image2", required = false) MultipartFile imageFiles2,
            @RequestParam(name = "image3", required = false) MultipartFile imageFiles3,
            @RequestParam(name = "image4", required = false) MultipartFile imageFiles4,
            @RequestParam(name = "image5", required = false) MultipartFile imageFiles5
    ) {
        try {
            ImgRoom addedRoom = ihostService.addImgRoom(idRoom, hostId, imageFiles1, imageFiles2, imageFiles3, imageFiles4, imageFiles5);

            if (addedRoom != null) {
                return new ResponseEntity<>(new ResponseAll(true, "ImgRoom added successfully"), HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseAll(false, "Failed to add room"));
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseAll(false, "Error: " + e.getMessage()));
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
        if ("Room confirmation successful!".equals(result)) {
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
        List<Room> roomsAndImages = ihostService.getAllRoomByHost(hostId);

        if (roomsAndImages.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(roomsAndImages);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            Map result = ihostService.uploadImage(file);
            String imageUrl = result.get("secure_url").toString();
            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to upload the image");
        }
    }
}