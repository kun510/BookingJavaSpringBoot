package com.example.doancuoiky.hostel.controller;

import com.example.doancuoiky.hostel.model.*;
import com.example.doancuoiky.hostel.request.AddRoom;
import com.example.doancuoiky.hostel.request.BillTotal;
import com.example.doancuoiky.hostel.request.RegisterRq;
import com.example.doancuoiky.hostel.service.IhostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

//    @PostMapping(value = "/addroom")
//    public ResponseEntity<String> addRoom(@RequestBody AddRoom addRoomRequest, @RequestParam int id_host,@RequestParam("image")MultipartFile file ) {
//        Room addedRoom = ihostService.addRoom(addRoomRequest, id_host,file) ;
//        if (addedRoom != null) {
//            return ResponseEntity.ok("ok add room.");
//        } else {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail add room");
//        }
//    }
//    @PostMapping("/addRoom")
//    public ResponseEntity<?> addRoom(@ModelAttribute AddRoom addRoomRequest, @RequestParam("hostId") long hostId, @RequestParam("image") MultipartFile imageFile) {
//        try {
//            Room addedRoom = ihostService.addRoom(addRoomRequest, hostId, imageFile);
//            if (addedRoom != null) {
//                return new ResponseEntity<>("Room added successfully", HttpStatus.OK);
//            } else {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add room");
//            }
//        } catch (IOException e) {
//            // Xử lý lỗi khi có ngoại lệ IOException
//            return new ResponseEntity<>("Failed to add room: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
    @PostMapping("/addRoom")
    public ResponseEntity<?> addRoom( @Valid @ModelAttribute  AddRoom addRoomRequest, @RequestParam("hostId") long hostId, @RequestParam("image") MultipartFile imageFile) {
        try {
            Room addedRoom = ihostService.addRoom(addRoomRequest, hostId, imageFile);

            if (addedRoom != null) {
                return new ResponseEntity<>("Room added successfully", HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add room");
            }
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to add room: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/fileSystem")
    public ResponseEntity<?> uploadImageToFIleSystem(@RequestBody AddRoom addRoom,@RequestParam("image")MultipartFile file) throws IOException {
        String uploadImage = ihostService.uploadImageToFileSystem(addRoom,file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }

    @GetMapping("/fileSystem/{fileName}")
    public ResponseEntity<?> downloadImagesFromFileSystem(@PathVariable String fileName) throws IOException {
        List<byte[]> imageList = ihostService.downloadImagesFromFileSystem(fileName);
        if (imageList.isEmpty()) {
            String errorMessage = "Không tìm thấy ảnh với tên " + fileName;
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", errorMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(errorResponse);
        } else {
            MediaType contentType = ihostService.determineMediaType(fileName);
            ByteArrayInputStream byteArrayInputStream = createInputStream(imageList);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(contentType);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new InputStreamResource(byteArrayInputStream));
        }
    }

    private ByteArrayInputStream createInputStream(List<byte[]> imageList) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (byte[] imageData : imageList) {
            byteArrayOutputStream.write(imageData);
        }
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
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
//        List<Room> room = ihostService.getAllRoomByHost(hostId);
//
//        if (!room.isEmpty()) {
//            return ResponseEntity.ok(room);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
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
