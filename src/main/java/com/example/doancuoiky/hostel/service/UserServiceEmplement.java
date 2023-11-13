package com.example.doancuoiky.hostel.service;


import com.example.doancuoiky.hostel.model.*;
import com.example.doancuoiky.hostel.repository.*;
import com.example.doancuoiky.hostel.request.RegisterRq;
import com.example.doancuoiky.hostel.request.ReportRq;
import com.example.doancuoiky.hostel.request.ReviewRq;
import com.example.doancuoiky.hostel.request.UpdateUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceEmplement implements IuserService, UserDetailsService{

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

    @Override
    public Users register(RegisterRq users) {
        if (users != null) {
            Users users1 = new Users();
            users1.setPhone(users.getPhone());
            users1.setAddress(users.getAddress());
            users1.setEmail(users.getEmail());
            users1.setImg(users.getImg());
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
    public Users updateuser(long id, UpdateUsers usermodels) {
        if (usermodels != null) {
            Users usModel = userRepository.getOne(id);
            if (usModel != null) {
                usModel.setName(usermodels.getName());
                usModel.setEmail(usermodels.getEmail());
                usModel.setAddress(usermodels.getAddress());
                usModel.setImg(usermodels.getImg());
                return userRepository.save(usModel);
            }
        }
        return null;
    }


    @Override
    public boolean deleteuser(long id) {
        if(id>=1){
            Users user = userRepository.getOne(id);
            if(user!=null){
                userRepository.delete(user);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Users> getAlluser() {
        return userRepository.findAll();
    }

    @Override
    public List<Room> getAllRoom() {
        return roomRepository.allRooms();
    }

    @Override
    public List<Room> getAllRoomHot() {
        return roomRepository.allRoomsHot();
    }

    @Override
    public List<TotalBill> CheckBill(long idRoom) {
        return billRepository.findAll();
    }

    @Override
    public Users getoneuser(long id) {
        return userRepository.getOne(id);
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
    public Rent Rent(long idRoom, long idUser) {
        Optional<Users> userOptional = userRepository.findById(idUser);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            Optional<Room> roomOptional = roomRepository.findById(idRoom);
            if (roomOptional.isPresent()) {
                Room room = roomOptional.get();
                Rent existingRent = rentRepository.findByUser(user);
                if (existingRent == null) {
                    Rent rentAdd = new Rent();
                    rentAdd.setUser(user);
                    rentAdd.setRoom(room);
                    rentAdd.setStatus("wait for confirmation");
                    return rentRepository.save(rentAdd);
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
    public Report Report(ReportRq report, long idRoom, long idUser) {
        Optional<Users> usersOptional = userRepository.findById(idUser);
        if (usersOptional.isPresent()){
            Users user = usersOptional.get();
            Optional<Room> roomOptional = roomRepository.findById(idRoom);
            if (roomOptional.isPresent()) {
                Room room = roomOptional.get();
                Date currentTime = new Date();
                Report reportAdd = new Report();
                reportAdd.setReason(report.getReason());
                reportAdd.setRoom(room);
                reportAdd.setUser(user);
                reportAdd.setDay(currentTime);
                return reportRepository.save(reportAdd);
            } else {
                throw new IllegalArgumentException("room not found");
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }

    }
    @Override
    public Review Review(ReviewRq reviewRq, long idRoom, long idUser) {
        Optional<Users> usersOptional = userRepository.findById(idUser);
        if (usersOptional.isPresent()){
            Users user = usersOptional.get();
            Optional<Room> roomOptional = roomRepository.findById(idRoom);
            if (roomOptional.isPresent()) {
                Room room = roomOptional.get();
                boolean hasReviewed = reviewReponsitory.existsByUserAndRoom(user,room);
                if (hasReviewed) {
                    throw new IllegalArgumentException("User has already reviewed a room");
                } else {
                    Date currentTime = new Date();
                    Review review = new Review();
                    review.setUser(user);
                    review.setRoom(room);
                    review.setEvaluate(reviewRq.getEvaluate());
                    review.setNumberOfStars(reviewRq.getNumberOfStars());
                    review.setDate(new Date(currentTime.getTime()));
                    return reviewReponsitory.save(review);
                }
            } else {
                throw new IllegalArgumentException("room not found");
            }
        } else {
            throw new IllegalArgumentException("user not found");
        }
    }

    @Override
    public List<Room> searchRooms(String address, String price, String area,String people,String type) {
        List<Room> rooms = new ArrayList<>();

        if (address != null && !address.isEmpty()) {
            rooms.addAll(roomRepository.findByAddressContaining(address));
        }

        else if (price != null && !price.isEmpty()) {
            rooms.addAll(roomRepository.findByPrice(price));
        }

        else if (area != null && !area.isEmpty()) {
            rooms.addAll(roomRepository.findByArea(area));
        }
        else if (people != null && !people.isEmpty()) {
            rooms.addAll(roomRepository.findByPeople(people));
        }
        else if (type != null && !type.isEmpty()) {
            rooms.addAll(roomRepository.findByType(type));
        }
        else {
            throw new IllegalArgumentException("room find no found");
        }
        return rooms;
    }

    @Override
    public List<TotalBill> getAllBill(long idUser) {
        return billRepository.findTotalBillsByUserId(idUser);
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
}
