package com.example.doancuoiky.hostel.service;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.doancuoiky.hostel.model.*;
import com.example.doancuoiky.hostel.repository.*;
import com.example.doancuoiky.hostel.request.*;
import com.example.doancuoiky.hostel.response.ResponseAll;
import com.example.doancuoiky.hostel.response.ResponseToken;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class UserServiceImplement implements IuserService, UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private IuserService userService;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RentRepository rentRepository;
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private ReviewReponsitory reviewReponsitory;
    @Autowired
    private ReviewBoardingReponsitory reviewBoardingReponsitory;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private RoomFavouriteRepository roomFavouriteRepository;
    @Autowired
    private BoardingRepository boardingRepository;
    @Autowired
    private ListImgRepository listImgRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private FirebaseMessaging firebaseMessaging;

    @Override
    public ResponseAll sendNotificationByToken(NotificationMessaging notificationMessaging){
        Notification notification = Notification.builder()
                .setTitle(notificationMessaging.getTitle())
                .setBody(notificationMessaging.getBody())
                .setImage(notificationMessaging.getImg())
                .build();
        Message message = Message.builder()
                .setToken(notificationMessaging.getToken())
                .setNotification(notification)
                .putAllData(notificationMessaging.getData())
                .build();
        try{
            firebaseMessaging.send(message);
            return new ResponseAll (true,"Success Sending Notification");
        }catch (FirebaseMessagingException exception){
            exception.printStackTrace();
            return new ResponseAll(false, "Error Sending Notification");
        }
    }



    @Override
    public Users register(RegisterRq users) {
        if (users != null) {
            Users users1 = new Users();
            users1.setPhone(users.getPhone());
            users1.setAddress(users.getAddress());
            users1.setEmail(users.getEmail());
            users1.setImg("users.getImg()");
            users1.setName(users.getName());
            Role role = new Role();
            role.setId(3L);
            users1.setRole(role);
            String maHoa = passwordEncoder.encode(users.getPassword());
            users1.setPassword(maHoa);

            Users savedUser = userRepository.save(users1);
            if (savedUser != null) {
                return savedUser;
            }
        }
        return null;
    }

    @Override
    public ResponseAll changePassword(long id,String password) {
        try {
            String maHoa = passwordEncoder.encode(password);
            userRepository.changePassword(maHoa,id);
            return new ResponseAll(true, "successfully saved token.");
        } catch (Exception e) {
            return new ResponseAll(false, "Error: " + e.getMessage());
        }
    }

    @Override
    public Users getUserByUsername(String phone) {
        return userRepository.findByPhone(phone);
    }

    @Override
    public Users login(String phone, String password) {
        Users user = userRepository.findByPhone(phone);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    //check phone
    public boolean isUserPhoneExists(String phone) {
        return userRepository.existsByPhone(phone);
    }

    @Override
    public Users updateuser(long id, UpdateUsers usermodels, MultipartFile avt) {
        if (usermodels != null) {
            Users usModel = userRepository.getOne(id);
            if (usModel != null) {
                usModel.setName(usermodels.getName());
                usModel.setEmail(usermodels.getEmail());
                usModel.setAddress(usermodels.getAddress());
                Map uploadResult1 = null;
                try {
                    uploadResult1 = uploadImageAvt(avt);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                usModel.setImg(uploadResult1.get("secure_url").toString());
                return userRepository.save(usModel);
            }
        }
        return null;
    }


    @Override
    public boolean deleteuser(long id) {
        if (id >= 1) {
            Users user = userRepository.getOne(id);
            if (user != null) {
                userRepository.delete(user);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteRoomFavourite(long idRoomFavourite, long idUser) {
        if (idRoomFavourite > 0 && idUser > 0){
           List<RoomFavourite> checkRoomFavourite = roomFavouriteRepository.checkRoomFavourite(idRoomFavourite,idUser);
            if (!checkRoomFavourite.isEmpty()) {
                roomFavouriteRepository.deleteById(idRoomFavourite);
                return true;
            } else {
                return false;
            }

        }
        return false;
    }

    @Override
    public List<Users> getAlluser() {
        return userRepository.findAll();
    }

    @Override
    public List<Users> getUserCurrent(long idUser) {
        return userRepository.getUserCurrent(idUser);
    }

    @Override
    public List<Room> getAllRoom() {
        return roomRepository.allRooms();
    }

    @Override
    public List<Boarding_host> allBoardingMap() {
        return boardingRepository.allBoardingMap();
    }

    @Override
    public List<Room> getAllRoomHot() {
        return roomRepository.allRoomsHot();
    }

    @Override
    public List<Rent> getMyRoom(long idUser) {
        return rentRepository.MyRoomByIdUser(idUser);
    }

    @Override
    public List<TotalBill> CheckBill(long idRoom) {
        return billRepository.findAll();
    }

    @Override
    public List<ImgRoom> ListImgInRoom(long idRoom) {
        return listImgRepository.allImgInRoom(idRoom) ;
    }

    @Override
    public List<Boarding_host> allList() {
        return boardingRepository.allBoarding();
    }

    @Override
    public Users getoneuser(long id) {
        return userRepository.getOne(id);
    }
    @Override
    public List<RoomFavourite> listRoomFavourite(long idUser) {
      Optional<Users> checkUser = userRepository.findById(idUser);
      if(checkUser.isPresent()){
          return roomFavouriteRepository.allRoomFavourite(idUser);
      }
       return null;
    }

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        Users user = userService.getUserByUsername(phone);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + phone);
        }
        return User.builder()
                .username(user.getPhone())
                .password(user.getPassword())
                .roles("String")
                .build();
    }

    @Override
    public Rent Rent(long idRoom, long idUser, int numberUserInRoom) {
        Optional<Users> userOptional = userRepository.findById(idUser);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            Optional<Room> roomOptional = roomRepository.findById(idRoom);
            if (roomOptional.isPresent()) {
                Room room = roomOptional.get();
               Rent existingRent = rentRepository.findUsersInRent(idRoom,idUser);
                if (existingRent == null) {
                    int usersInRoom = roomRepository.numberUserInRoom(idRoom);
                    if (numberUserInRoom <= usersInRoom){
                        Rent rentAdd = new Rent();
                        rentAdd.setUser(user);
                        rentAdd.setRoom(room);
                        rentAdd.setStatus("wait for confirmation");
                        rentAdd.setPeopleInRoom(numberUserInRoom);
                        NotificationApp notification = new NotificationApp();
                        notification.setUser_id_sender(user);
                        notification.setContent("user: "+ user.getName()+", " + "rented "+ "number room: " +room.getNumberRoom()+", address: " + room.getBoardingHostel().getAddress());
                        Users idHost = roomRepository.findHostByIdRoom(idRoom);
                        notification.setUser_id_receiver(idHost);
                        Date currentTime = new Date();
                        notification.setTime(new Date(currentTime.getTime()));
                        notificationRepository.save(notification);
                        return rentRepository.save(rentAdd);
                    }
                    else {
                        throw new IllegalStateException("The number of users cannot be more than allowed");
                    }

                } else {
                    throw new IllegalStateException("User has already rented a room");
                }
            } else {
                throw new IllegalArgumentException("Room not found");
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    @Override
    public ResponseAll Report(ReportRq report, MultipartFile imageFile1, MultipartFile imageFile2, MultipartFile imageFile3, long idRoom, long idUser) {
        if (reportRepository.findUsersInReport(idRoom, idUser) > 0) {
            return new ResponseAll(false, "User has already reported this room.");
        }

            Optional<Users> usersOptional = userRepository.findById(idUser);
            int findBoarding = rentRepository.findBoardingIdByRoomId(idRoom);
            List<Rent> checkUserBelongsToBoarding = rentRepository.checkUserBelongsToBoarding(idUser, idRoom);

            if (findBoarding > 0 && checkUserBelongsToBoarding != null) {
                if (usersOptional.isPresent()) {
                    Users user = usersOptional.get();
                    Optional<Room> roomOptional = roomRepository.findById(idRoom);
                    if (roomOptional.isPresent()) {
                        Room room = roomOptional.get();
                        Date currentTime = new Date();
                        Report reportAdd = new Report();
                        int checkRoom = reportRepository.checkRoom(idRoom);
                        if (checkRoom > 0) {
                            reportRepository.updateNumberReport(idRoom);
                            try {
                                Map uploadResult1 = uploadImage(imageFile1);
                                reportAdd.setImg1(uploadResult1.get("secure_url").toString());
                                Map uploadResult2 = uploadImage(imageFile2);
                                reportAdd.setImg2(uploadResult2.get("secure_url").toString());
                                Map uploadResult3 = uploadImage(imageFile3);
                                reportAdd.setImg3(uploadResult3.get("secure_url").toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                                return new ResponseAll(false, "Error uploading images.");
                            }
                        } else {
                            try {
                                Map uploadResult1 = uploadImage(imageFile1);
                                reportAdd.setImg1(uploadResult1.get("secure_url").toString());
                                Map uploadResult2 = uploadImage(imageFile2);
                                reportAdd.setImg2(uploadResult2.get("secure_url").toString());
                                Map uploadResult3 = uploadImage(imageFile3);
                                reportAdd.setImg3(uploadResult3.get("secure_url").toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                                return new ResponseAll(false, "Error uploading images.");
                            }
                            reportAdd.setTimes_report(1);
                        }
                        reportAdd.setReason(report.getReason());
                        reportAdd.setRoom(room);
                        reportAdd.setUser(user);
                        reportAdd.setDay(currentTime);
                        reportRepository.save(reportAdd);
                        return new ResponseAll(true, "The report has been successfully saved.");
                    } else {
                        return new ResponseAll(false, "Room not found.");
                    }
                } else {
                    return new ResponseAll(false, "User not found.");
                }
            } else {
                return new ResponseAll(false, "User not in Boarding Hostel");
            }

    }


    @Override
    public Review Review(ReviewRq reviewRq, long idRoom, long idUser) {
        Optional<Users> usersOptional = userRepository.findById(idUser);
        if (usersOptional.isPresent()) {
            Users user = usersOptional.get();
            Users usersCheck = getUserWithRole(idUser,3);
            Optional<Room> roomOptional = roomRepository.findById(idRoom);
            if (roomOptional.isPresent()) {
                Room room = roomOptional.get();
                boolean hasReviewed = reviewReponsitory.existsByUserAndRoom(user, room);
                if (hasReviewed) {
                    throw new IllegalArgumentException("User has already reviewed a room");
                } else {
                    List<Rent> checkUserBelongsToBoarding = rentRepository.checkUserBelongsToBoarding(idUser,idRoom);
                    if (checkUserBelongsToBoarding != null && !checkUserBelongsToBoarding.isEmpty()){
                        Date currentTime = new Date();
                        Review review = new Review();
                        review.setUser(user);
                        review.setRoom(room);
                        review.setEvaluate(reviewRq.getEvaluate());
                        review.setNumberOfStars(reviewRq.getNumberOfStars());
                        review.setDate(new Date(currentTime.getTime()));
                        return reviewReponsitory.save(review);
                    }else {
                        throw new IllegalArgumentException("User not Rent in Room");
                    }
                }
            } else {
                throw new IllegalArgumentException("room not found");
            }
        } else {
            throw new IllegalArgumentException("user not found");
        }
    }

    @Override
    public ReviewBoarding ReviewBoarding(ReviewRq reviewRq, long idBoarding, long idUser) {
        Optional<Users> usersOptional = userRepository.findById(idUser);
        if (usersOptional.isPresent()) {
            Users user = usersOptional.get();
            Users usersCheck = getUserWithRole(idUser,3);
            Optional<Boarding_host> roomOptional = boardingRepository.findById(idBoarding);
            if (roomOptional.isPresent()) {
                Boarding_host room = roomOptional.get();
                boolean hasReviewed = reviewBoardingReponsitory.existsByUserAndBoarding(user, room);
                if (hasReviewed) {
                    throw new IllegalArgumentException("User has already reviewed a room");
                } else {
                    List<Rent> checkUserBelongsTo = rentRepository.checkUserBelongsTo(idUser,idBoarding);
                    if (checkUserBelongsTo != null && !checkUserBelongsTo.isEmpty()){
                        Date currentTime = new Date();
                        ReviewBoarding review = new ReviewBoarding();
                        review.setUser(user);
                        review.setBoarding(room);
                        review.setEvaluate(reviewRq.getEvaluate());
                        review.setNumberOfStars(reviewRq.getNumberOfStars());
                        review.setDate(new Date(currentTime.getTime()));
                        return reviewBoardingReponsitory.save(review);
                    }else {
                        throw new IllegalArgumentException("User not Rent in Room");
                    }
                }
            } else {
                throw new IllegalArgumentException("room not found");
            }
        } else {
            throw new IllegalArgumentException("user not found");
        }
    }

    private Users getUserWithRole(long userId, int roleId) {
        Optional<Users> usersOptional = userRepository.findById(userId);
        if (!usersOptional.isPresent() || usersOptional.get().getRole().getId() != roleId  ) {
            throw new RuntimeException("Người dùng không phải là User");
        }
        return usersOptional.get();
    }

    @Override
    public List<Room> searchRooms(Integer price, String area, String people, String type) {
        List<Room> rooms = new ArrayList<>();

        if (price != null && price != 0) {
            rooms.addAll(roomRepository.findByPrice(price));
        }

        else if (area != null && !area.isEmpty()) {
            rooms.addAll(roomRepository.findByArea(area));
        }
        else if (people != null && !people.isEmpty()) {
            rooms.addAll(roomRepository.findByPeople(people));

        } else if (type != null && !type.isEmpty()) {
            rooms.addAll(roomRepository.findByType(type));
        } else {
            throw new IllegalArgumentException("room find no found");
        }
        return rooms;
    }

    @Override
    public List<TotalBill> getAllBill(long idUser) {
        return billRepository.findTotalBillsByUserId(idUser);
    }

    @Override
    public List<Boarding_host> getBoarding(String area) {
        return boardingRepository.allRooms(area);
    }

    @Override
    public List<Room> allRoomByBoarding(long boardingId) {
        return roomRepository.allRoomByBoarding(boardingId);
    }

    @Override
    public ResponseAll addTokenDevice(String token, long userID) {
        try {
            userRepository.addToken(token, userID);
            return new ResponseAll(true, "successfully saved token.");
        } catch (Exception e) {
            return new ResponseAll(false, "Error: " + e.getMessage());
        }
    }
    @Override
    public ResponseAll addRoomFavourite(long userId, long roomId) {
        try{
            RoomFavourite roomFavourite = new RoomFavourite();
            roomFavourite.setUser(userRepository.findById(userId).orElse(null));
            roomFavourite.setRoom(roomRepository.findById(roomId).orElse(null));
            if (roomFavourite.getUser() != null && roomFavourite.getRoom() != null) {
                LocalDateTime currentTime = LocalDateTime.now();
                Date currentDateTime = Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant());
                roomFavourite.setDay(currentDateTime);

                roomFavouriteRepository.save(roomFavourite);
                return new ResponseAll(true, "Add Room Favourite Successfully");
            } else {
                return new ResponseAll(false, "User or Room not found");
            }
        }catch (Exception e){
            return new ResponseAll(false, "Error: " + e.getMessage());
        }
    }

    @Override
    public List<NotificationApp> listNotificationReceiver(long userId) {
        return notificationRepository.allNotificationReceiver(userId);
    }

    @Override
    public ResponseToken getToken(long idUser) {
        try {
            String token = userRepository.getToken(idUser);
            if (token != null) {
               return new ResponseToken(true,"token Successfully",token);
            } else {
               return new ResponseToken(false,"token not found",null);
            }
        } catch (Exception e) {
            return new ResponseToken(false, "Error: ", e.getMessage());
        }
    }

    @Override
    public List<Users> UserCurrent(long UserId) {
        return userRepository.UserCurrent(UserId);
    }

    @Override
    public List<Review> ReviewByRoom(long roomId) {
        return reviewReponsitory.allReviewByRoom(roomId);
    }

    @Override
    public List<Review> ReviewByHost(long hostId) {
        return reviewReponsitory.allReviewByHost(hostId);
    }

    @Override
    public List<String> tokenUser(long hostId) {
        return rentRepository.tokenUser(hostId);
    }


    @Scheduled(cron = "0 40 7 * * ?")
    public void updateRoomAverageStars() {
        List<Room> rooms = roomRepository.allRooms();
        for (Room room : rooms) {
            List<Review> reviews = reviewReponsitory.findByRoom(room);
            if (!reviews.isEmpty()) {
                double totalStars = 0;
                for (Review review : reviews) {
                    totalStars += review.getNumberOfStars();
                }
                double averageStars = totalStars / reviews.size();
                room.setNumberOfStars((int) averageStars);
                roomRepository.save(room);
            }
        }
    }

    @Scheduled(cron = "0 40 7 * * ?")
    public void updateBoardingAverageStars() {
        List<Boarding_host> rooms = boardingRepository.allBoarding();
        for (Boarding_host boardingHost : rooms) {
            List<Review> reviews = reviewReponsitory.allRoomByBoarding(boardingHost);
            if (!reviews.isEmpty()) {
                double totalStars = 0;
                for (Review review : reviews) {
                    totalStars += review.getNumberOfStars();
                }
                double averageStars = totalStars / reviews.size();
                boardingHost.setNumberOfStars((float) averageStars);
                boardingRepository.save(boardingHost);
            }
        }
    }
    public Map uploadImage(MultipartFile file) throws IOException {
        // String publicId = "Hostel/" + UUID.randomUUID().toString() + System.currentTimeMillis();
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String publicId = "Report/" + originalFilename;
        Map params = ObjectUtils.asMap(
                "public_id", publicId,
                "folder", "Hostel"
        );
        Map result = cloudinary.uploader().upload(file.getBytes(), params);
        return result;
    }
    public Map uploadImageAvt(MultipartFile file) throws IOException {
        // String publicId = "Hostel/" + UUID.randomUUID().toString() + System.currentTimeMillis();
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String publicId = "User/" + originalFilename;
        Map params = ObjectUtils.asMap(
                "public_id", publicId,
                "folder", "Hostel"
        );
        Map result = cloudinary.uploader().upload(file.getBytes(), params);
        return result;
    }
}
