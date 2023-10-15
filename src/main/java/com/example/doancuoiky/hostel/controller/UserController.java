package com.example.doancuoiky.hostel.controller;

import com.example.doancuoiky.hostel.model.*;
import com.example.doancuoiky.hostel.request.*;
import com.example.doancuoiky.hostel.response.Response;
import com.example.doancuoiky.hostel.response.ResponseAll;
import com.example.doancuoiky.hostel.service.IuserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    @GetMapping("/getallroomhot")
    public List<Room> getAllRoomsHot() {
        return iuserService.getAllRoomHot();
    }
    @PostMapping("/register")
    public ResponseEntity<ResponseAll> register(@RequestBody @Valid RegisterRq registerRequest) {
        try {
            if (iuserService.isUserPhoneExists(registerRequest.getPhone())) {
                ResponseAll response = new ResponseAll(false, "User with the provided phone already exists.");
                return ResponseEntity.badRequest().body(response);
            } else {
                Users registeredUser = iuserService.register(registerRequest);
                if (registeredUser != null && registeredUser.getId() > 0) {
                    ResponseAll response = new ResponseAll(true, "Register successfully");
                    return ResponseEntity.status(HttpStatus.CREATED).body(response);
                } else {
                    ResponseAll response = new ResponseAll(false, "Check");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                }
            }
        } catch (Exception e) {
            ResponseAll response = new ResponseAll(false, "Register Fail because of DB error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRq loginRequest) {
        Users user = iuserService.login(loginRequest.getPhone(), loginRequest.getPassword());
        if (user != null) {
            Role userRole = user.getRole();
            if (userRole != null) {
                if (userRole.getId() == 1) {
                    return ResponseEntity.ok(new Response("Admin"));
                } else if (userRole.getId() == 2) {
                    return ResponseEntity.ok(new Response("Host"));
                } else if (userRole.getId() == 3) {
                    return ResponseEntity.ok(new Response("User"));
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Unknown role"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("User has no role assigned"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("check username or password"));
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
