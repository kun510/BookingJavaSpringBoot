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
import org.springframework.web.multipart.MultipartFile;

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
    @GetMapping("/getAllRoomByBoarding")
    public List<Room> allRoomByBoarding(@RequestParam long boardingId) {
        return iuserService.allRoomByBoarding(boardingId);
    }
    @GetMapping("/getAllBoarding")
    public List<Boarding_host> getAllBoarding() {
        return iuserService.allList();
    }
    @GetMapping("/getImgInRoom")
    public List<ImgRoom> getImgInRoom(@RequestParam long idRoom) {
        return iuserService.ListImgInRoom(idRoom);
    }
    @GetMapping("/getNotificationReceiver")
    public List<NotificationApp> allNotificationReceiver(@RequestParam long idUserReceiver) {
        return iuserService.listNotificationReceiver(idUserReceiver);
    }
    @GetMapping("/getMyRoom")
    public List<Rent> MyRoom(@RequestParam long UserId) {
        return iuserService.getMyRoom(UserId);
    }
    @GetMapping("/getallboardingNear")
    public List<Boarding_host> getAllBoarding(@RequestParam String area) {
        return iuserService.getBoarding(area);
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
                    return ResponseEntity.ok(new Response("Admin",user.getId()));
                } else if (userRole.getId() == 2) {
                    return ResponseEntity.ok(new Response("Host",user.getId()));
                } else if (userRole.getId() == 3) {
                    return ResponseEntity.ok(new Response("User",user.getId()));
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Unknown role",null));
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("User has no role assigned",null));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("check username or password",null));
        }
    }


    //update
    @PutMapping("/update")
    public Users updateuser(@RequestParam("id") long id, @RequestBody UpdateUsers user){
        return iuserService.updateuser(id, user);
    }
    @GetMapping("/getRoomFavourite")
    public List<RoomFavourite> getAllRoomFavourite(@RequestParam("idUser") long idUser){
        return iuserService.listRoomFavourite(idUser);
    }
    //addtoken
    @PutMapping("/addtoken")
    public ResponseEntity<String> addtoken(@RequestParam("token_device") String token_device, @RequestParam("userID") long userID) {
        ResponseAll response = iuserService.addTokenDevice(token_device, userID);

        if (response.isSuccess()) {
            return ResponseEntity.ok("Token added successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + response.getMessage());
        }
    }
    @PostMapping("/notification")
    public ResponseEntity<?> sendNotification(@RequestBody NotificationMessaging notificationMessaging){
        ResponseAll responseAll =  iuserService.sendNotificationByToken(notificationMessaging);
        if (responseAll.isSuccess()){
            return ResponseEntity.ok(iuserService.sendNotificationByToken(notificationMessaging));
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + responseAll.getMessage());
        }
    }

    //delete
    @DeleteMapping("/delete/{id}")
    public boolean  deleteuser(@PathVariable long id){
        return iuserService.deleteuser(id);
    }

    @DeleteMapping("/deleteRoomFavourite")
    public boolean  deleteRoomFavourite(@RequestParam long idRoomFavourite,long idUser){
       boolean deleteRoom = iuserService.deleteRoomFavourite(idRoomFavourite,idUser);
       if (deleteRoom){
           return true;
       }else {
           return false;
       }
    }

    @PostMapping("/rent")
    public ResponseEntity<?> rent(@RequestParam long idRoom,long idUser, int numberUserInRoom) {
        try {
            Rent result = iuserService.Rent(idRoom, idUser,numberUserInRoom);
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
    @PostMapping("/addRoomFavourite")
    public ResponseEntity<?> RoomFavourite(@RequestParam long idRoom,long idUser) {
        try {
            ResponseAll result = iuserService.addRoomFavourite(idUser, idRoom);
            if (result != null) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register room rental");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error: " + e.getMessage());
        }
    }

    @PostMapping("/report")
    public ResponseEntity<ResponseAll> report(@Valid @ModelAttribute ReportRq report, @RequestParam("image1") MultipartFile imageFile1, @RequestParam("image2") MultipartFile imageFile2, @RequestParam("image3") MultipartFile imageFile3, @RequestParam long idRoom, @RequestParam long idUser) {
        try {
            ResponseAll response = iuserService.Report(report, imageFile1, imageFile2, imageFile3, idRoom, idUser);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseAll(false, "Internal server error: " + e.getMessage()));
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
    @PostMapping("/ReviewBoarding")
    public ResponseEntity<?> ReviewBoarding(@RequestBody ReviewRq reviewRq,@RequestParam long idBoarding,@RequestParam long idUser){
        try {
            Review review = iuserService.ReviewBoarding(reviewRq,idBoarding,idUser);
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
