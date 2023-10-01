package com.example.doancuoiky.hostel.controller;

import com.example.doancuoiky.hostel.model.*;
import com.example.doancuoiky.hostel.request.*;
import com.example.doancuoiky.hostel.service.IuserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "/user")
public class UserController {
    @Autowired
    private IuserService iuserService;

    @GetMapping("/getuser")
    public List<Users> getAlluser(){
        return iuserService.getAlluser();
    }
    @GetMapping("/getallroom")
    public List<Room> getAllRooms() {
        return iuserService.getAllRoom();
    }
    @PostMapping("/register")
    public ResponseEntity<String>register(@RequestBody RegisterRq users){
        ResponseEntity<String> notification = null;
        try{
            if (iuserService.isUserPhoneExists(users.getPhone())) {
                return ResponseEntity.badRequest().body("User with the provided phone already exists.");
            }else {
                Users register = iuserService.register(users);
                if (register.getId() > 0) {
                    notification = ResponseEntity.status(HttpStatus.CREATED).body("Register successfully");
                }
                else {
                    notification = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("check");
                }
            }
        }
        catch (Exception e){
            notification = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Register Fail because of DB error ");
        }
        return notification;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRq loginRequest) {
        Users user = iuserService.login(loginRequest.getPhone(), loginRequest.getPassword());
        if (user != null) {
            Role userRole = user.getRole();
            if (userRole != null) {
                if (userRole.getId() == 1) {
                    return ResponseEntity.ok("Admin");
                } else if (userRole.getId() == 2) {
                    return ResponseEntity.ok("Host");
                } else if (userRole.getId() == 3) {
                    return ResponseEntity.ok("User");
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unknown role");
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User has no role assigned");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("check username or password");
        }
    }

    //update
    @PutMapping("/update")
    public Users updateuser(@RequestParam("id") long id, @RequestBody UpdateUsers user){
        return iuserService.updateuser(id, user);
    }
    //delete
    @DeleteMapping("/delete/{id}")
    public boolean  deleteuser(@PathVariable long id){
        return iuserService.deleteuser(id);
    }

    @PostMapping("/rent")
    public ResponseEntity<?> rent(@RequestParam long idRoom,long idUser) {
        try {
            Rent result = iuserService.Rent(idRoom, idUser);
            if (result != null) {
                return ResponseEntity.ok("Registered room rental successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register room rental");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error: " + e.getMessage());
        }
    }
    @PostMapping("/report")
    public ResponseEntity<?> report(@RequestBody ReportRq report,@RequestParam long idRoom,@RequestParam long idUser){
        try {
            Report reportAdd = iuserService.Report(report,idRoom,idUser);
            if (reportAdd != null){
                return ResponseEntity.ok("Thanks for reporting");
            }else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to report");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error: " + e.getMessage());
        }
    }

    @PostMapping("/review")
    public ResponseEntity<?> review(@RequestBody ReviewRq reviewRq,@RequestParam long idRoom,@RequestParam long idUser){
        try {
            Review review = iuserService.Review(reviewRq,idRoom,idUser);
            if (review != null){
                return ResponseEntity.ok("Thanks for review");
            }else {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to review");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchRooms(
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String price,
            @RequestParam(required = false) String area,
            @RequestParam(required = false) String people,
            @RequestParam(required = false) String type
    ) {
        try {
            List<Room> rooms = iuserService.searchRooms( address,  price,  area, people, type);
            return ResponseEntity.ok(rooms);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error: " + e.getMessage());
        }
    }
    @GetMapping("/getbill")
    public ResponseEntity<?>  getAllBill(@RequestParam long idUser){
        List<TotalBill> totalBills = iuserService.getAllBill(idUser);
        if (totalBills.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No bill yet");
        } else {
            return ResponseEntity.ok(totalBills);
        }
    }

}
