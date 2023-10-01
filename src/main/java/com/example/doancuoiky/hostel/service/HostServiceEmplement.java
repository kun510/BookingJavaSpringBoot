package com.example.doancuoiky.hostel.service;

import com.example.doancuoiky.hostel.model.*;
import com.example.doancuoiky.hostel.repository.*;
import com.example.doancuoiky.hostel.request.AddRoom;
import com.example.doancuoiky.hostel.request.BillTotal;
import com.example.doancuoiky.hostel.request.RegisterRq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class HostServiceEmplement implements IhostService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private RentRepository rentRepository;


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
            role.setId(2L);
            users1.setRole(role);
            users1.setConfirmation_status("wait for confirmation");
            String mahoa = passwordEncoder.encode(users.getPassword());
            users1.setPassword(mahoa);

            Users savedUser = userRepository.save(users1);
            if (savedUser != null) {
                return savedUser;
            }
        }
        return null;
    }

    @Override
    public boolean isUserPhoneExists(String phone) {
        return userRepository.existsByPhone(phone);
    }


    @Override
    public Room addRoom(AddRoom addRoom, long hostId) {
        if (addRoom != null) {
            Optional<Users> usersOptional = userRepository.findById(hostId);
            if (usersOptional.isPresent()) {
                Users host = usersOptional.get();
               if(host.getRole().getId() == 2){
                   Room room = new Room();
                   room.setAddress(addRoom.getAddress());
                   room.setArea(addRoom.getArea());
                   room.setDescription(addRoom.getDescription());
                   room.setImg(addRoom.getImg());
                   room.setNumberRoom(addRoom.getNumberRoom());
                   room.setStatus(addRoom.getStatus());
                   room.setUtilityBills(addRoom.getUtilityBills());
                   room.setPrice(addRoom.getPrice());
                   room.setPeople(addRoom.getPeople());
                   room.setType(addRoom.getType());
                   room.setUser(host);
                   try {
                       return roomRepository.save(room);
                   } catch (Exception e) {
                       throw new RuntimeException("fail add room: " );
                   }
               }else {
                   throw new RuntimeException("User does not have the required role to add a room.");
               }
            }
        }
        return null;
    }

    @Override
    public TotalBill Bill(BillTotal totalBill, long idRent) {
        if (totalBill != null){
            Optional<Rent> rentOptional =  rentRepository.findById(idRent);
            if (rentOptional.isPresent()){
                Rent rent = rentOptional.get();
                if (!"Confirmed successfully".equals(rent.getStatus())) {
                    throw new RuntimeException("Cannot add bill for unconfirmed rent");
                }
                Date date = totalBill.getMonth();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int month = calendar.get(Calendar.MONTH) + 1;
                Optional<TotalBill> existingBillOptional = billRepository.findByRentAndMonth(rent, month);
                if (existingBillOptional.isPresent()) {
                    throw new RuntimeException("bill exists");
                }
                TotalBill Bill = new TotalBill();
                Bill.setElectricBill(totalBill.getElectricBill());
                Bill.setWaterBill(totalBill.getWaterBill());
                Bill.setCostsIncurred(totalBill.getCostsIncurred());
                Bill.setMonth(month);
                Bill.setRent(rent);
                int sum = totalBill.getElectricBill() + totalBill.getWaterBill() + totalBill.getCostsIncurred();
                Bill.setSum(sum);
                try {
                    billRepository.save(Bill);
                } catch (Exception e) {
                    throw new RuntimeException("fail add bill: " + e.getMessage());
                }
                return Bill;
            }
        }
        return null;
    }

    @Override
    public String UpdateRoom(long id, AddRoom UpdateRoom) {
        if (UpdateRoom != null) {
            Room usRoom = roomRepository.getOne(id);
            if (usRoom != null) {
                usRoom.setArea(UpdateRoom.getArea());
                usRoom.setAddress(UpdateRoom.getAddress());
                usRoom.setUtilityBills(UpdateRoom.getUtilityBills());
                usRoom.setDescription(UpdateRoom.getDescription());
                usRoom.setImg(UpdateRoom.getImg());
                usRoom.setNumberRoom(UpdateRoom.getNumberRoom());
                usRoom.setStatus(UpdateRoom.getStatus());
                usRoom.setPrice(UpdateRoom.getPrice());
                usRoom.setPeople(UpdateRoom.getPeople());
                usRoom.setType(UpdateRoom.getType());
                Room updatedRoom = roomRepository.save(usRoom);
                if (updatedRoom != null) {
                    return "Room updated successfully";
                } else {
                    return "Room update failed";
                }
            }
        }
        return "Unable to update room";
    }


    @Override
    public String DeleteRoom(long id) {
        if (id >= 1) {
            Optional<Room> roomOptional = roomRepository.findById(id);
            if (roomOptional.isPresent()) {
                roomRepository.delete(roomOptional.get());
                return "Room deleted successfully";
            } else {
                return "Room with ID " + id + " not found";
            }
        } else {
            return "Invalid ID";
        }
    }

    @Override
    public String AddUserInRoom(long roomId) {
        rentRepository.updateRentStatusByRoomId(roomId);
        roomRepository.updateRoomStatusById(roomId);
        Optional<Rent> updatedRentOptional = rentRepository.findById(roomId);
        if (updatedRentOptional.isPresent()) {
            Rent updatedRent = updatedRentOptional.get();
            if ("Confirmed successfully".equals(updatedRent.getStatus())) {
                return "Room confirmation successful!";
            } else {
                return "Room confirmation failed.";
            }
        } else {
            return "Room ID not found.";
        }
    }


    @Override
    public List<Rent> getAllRentConfirm(long hostId) {
        return rentRepository.findRentByHostId(hostId);
    }

    @Override
    public List<Users> getAllUser(long hostId) {
        return rentRepository.findUsersByHostId(hostId);
    }

    @Override
    public List<Room> getAllRoomByHost(long hostId) {
        return roomRepository.allRoomsEmpty(hostId);
    }
}
