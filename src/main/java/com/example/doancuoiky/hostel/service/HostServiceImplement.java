package com.example.doancuoiky.hostel.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.doancuoiky.hostel.model.*;
import com.example.doancuoiky.hostel.repository.*;
import com.example.doancuoiky.hostel.request.AddBoarding;
import com.example.doancuoiky.hostel.request.AddRoom;
import com.example.doancuoiky.hostel.request.BillTotal;
import com.example.doancuoiky.hostel.request.RegisterRq;
import com.example.doancuoiky.hostel.response.ResponseAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HostServiceImplement implements IhostService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BoardingRepository boardingRepository;
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private RentRepository rentRepository;
    @Autowired
    private ListImgRepository listImgRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private Cloudinary cloudinary;

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
    public ResponseAll addRoom(AddRoom addRoom, long boardingId, long hostId, MultipartFile imageFile) {
        ResponseAll response = new ResponseAll(false, "");
        try {
            if (addRoom != null) {
                Users host = getUserWithRole(hostId, 2);
                ResponseAll checkBan = checkBan(hostId);
                if (checkBan.isSuccess()) {
                    Boarding_host boardingHostel = getBoardingIfExists(hostId, boardingId);
                    if (boardingRepository.existsByStatusAndId("confirm", boardingId)) {
                        Room room = new Room();
                        room.setDescription(addRoom.getDescription());
                        room.setNumberRoom(addRoom.getNumberRoom());
                        room.setStatus("empty room");
                        room.setElectricBill(addRoom.getElectricBill());
                        room.setWaterBill(addRoom.getWaterBill());
                        room.setPrice(addRoom.getPrice());
                        room.setPeople(addRoom.getPeople());
                        room.setType(addRoom.getType());
                        room.setUser(host);
                        room.setBoardingHostel(boardingHostel);
                        Map uploadResult = uploadImage(imageFile);
                        room.setImg(uploadResult.get("secure_url").toString());
                        roomRepository.save(room);
                        boardingRepository.updateBoardingNumber(boardingId);
                        response.setSuccess(true);
                        response.setMessage("Room added successfully");
                    } else {
                        response.setMessage("Boarding_host status must be 'confirm' to add a room.");
                    }
                } else {
                    response.setMessage(checkBan.getMessage());
                }


            } else {
                response.setMessage("User does not have the required role to add a room.");
            }
        } catch (IOException e) {
            response.setMessage("Failed to upload the image to Cloudinary: " + e.getMessage());
        }

        return response;
    }

    @Override
    public ResponseAll addBoarding_hostel(AddBoarding addBoarding, long hostId, MultipartFile imageFile) {
        ResponseAll response = new ResponseAll(false, "");
        try {
            if (addBoarding != null) {
                Users host = getUserWithRole(hostId, 2);
                ResponseAll checkBan = checkBan(hostId);
                if (checkBan.isSuccess()) {
                    Boarding_host room = new Boarding_host();
                    room.setArea(addBoarding.getArea());
                    room.setAddress(addBoarding.getAddress());
                    room.setStatus("Waiting Confirm");
                    room.setUser(host);
                    room.setNumberRoom(0);
                    Map uploadResult = uploadImage(imageFile);
                    room.setImg(uploadResult.get("secure_url").toString());
                    NotificationApp notification = new NotificationApp();
                    notification.setUser_id_sender(host);
                    notification.setContent("user: " + host.getName() + ", " + "add " + "boarding hostel address: " + room.getAddress());
                    long checkAdmin = userRepository.checkAdmin();
                    if (checkAdmin == 0) {
                        response.setMessage("Admin user not found.");
                        return response;
                    }
                    Users adminId = userRepository.findById(checkAdmin).orElse(null);
                    if (adminId == null) {
                        response.setMessage("Admin user not found.");
                        return response;
                    }

                    notification.setUser_id_receiver(adminId);
                    Date currentTime = new Date();
                    notification.setTime(new Date(currentTime.getTime()));
                    notificationRepository.save(notification);
                    boardingRepository.save(room);
                    response.setSuccess(true);
                    response.setMessage("Boarding added successfully");
                } else {
                    response.setMessage(checkBan.getMessage());
                }
            } else {
                response.setMessage("Input data (addBoarding) is null.");
            }
        } catch (IOException e) {
            response.setMessage("Failed to upload the image to Cloudinary: " + e.getMessage());
        }
        return response;
    }


    @Override
    public TotalBill Bill(BillTotal totalBill, long idRent, long hostId) {
        if (totalBill != null) {
            Optional<Rent> rentOptional = rentRepository.findById(idRent);
            Users host = getUserWithRole(hostId, 2);
            if (rentOptional.isPresent()) {
                Rent rent = rentOptional.get();
                if (!"Confirmed successfully".equals(rent.getStatus())) {
                    throw new RuntimeException("Cannot add bill for unconfirmed rent");
                }
                Calendar calendar = Calendar.getInstance();
                int month = calendar.get(Calendar.MONTH) + 1;
                int year = calendar.get(Calendar.YEAR);
                String formattedMonthYear = String.format("%02d/%d", month, year);
                Optional<TotalBill> existingBillOptional = billRepository.findByRentAndMonth(rent, formattedMonthYear);
                if (existingBillOptional.isPresent()) {
                    throw new RuntimeException("bill exists");
                }
                TotalBill Bill = new TotalBill();
                Bill.setElectricBill(totalBill.getElectricBill());
                long idRoom = rentRepository.idRoomByRent(idRent);
                int setWaterInUser = rentRepository.peopleInRoom(idRoom);
                int setPriceWater = roomRepository.priceWater(idRoom);
                int sumWater = setWaterInUser * setPriceWater;
                Bill.setWaterBill(sumWater);
                Bill.setCostsIncurred(totalBill.getCostsIncurred());
                Bill.setMonth(formattedMonthYear);
                Bill.setRent(rent);
                int sum = totalBill.getElectricBill() + Bill.getWaterBill() + totalBill.getCostsIncurred();
                Bill.setSum(sum);
                try {
                    NotificationApp notification = new NotificationApp();
                    notification.setUser_id_sender(host);
                    notification.setContent("host: " + host.getName() + ", " + "added bill room: " + rent.getRoom().getNumberRoom()
                            + " month: " + Bill.getMonth());
                    long checkUserRent = rent.getUser().getId();
                    if (checkUserRent == 0) {
                        return null;
                    }
                    Users userId = userRepository.findById(checkUserRent).orElse(null);
                    if (userId == null) {
                        return null;
                    }
                    notification.setUser_id_receiver(userId);
                    Date currentTime = new Date();
                    notification.setTime(new Date(currentTime.getTime()));
                    notificationRepository.save(notification);
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
                usRoom.setWaterBill(UpdateRoom.getWaterBill());
                usRoom.setElectricBill(UpdateRoom.getElectricBill());
                usRoom.setDescription(UpdateRoom.getDescription());
                usRoom.setNumberRoom(UpdateRoom.getNumberRoom());
                usRoom.setStatus("empty room");
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
    public String DeleteRoom(Long id) {
        if (id >= 1) {
            Optional<Room> roomOptional = roomRepository.findById(Long.valueOf(id));
            if (roomOptional.isPresent()) {
                listImgRepository.deleteByRoomId(id);
                roomRepository.delete(roomOptional.get());
                return "Room with ID " + id + " deleted successfully";
            } else {
                return "Room with ID " + id + " not found";
            }
        } else {
            return "Invalid ID";
        }
    }

    @Override
    public String AddUserInRoom(long roomId) {
        Optional<Rent> updatedRentOptional = rentRepository.findById(roomId);
        if (updatedRentOptional.isPresent()) {
            rentRepository.updateRentStatusByRoomId(roomId);
            roomRepository.updateRoomStatusById(roomId);
            Rent updatedRent = updatedRentOptional.get();
            if ("Confirmed successfully".equals(updatedRent.getStatus())) {
                NotificationApp notificationApp = new NotificationApp();
                notificationApp.setUser_id_sender(rentRepository.idHostByRent(roomId));
                notificationApp.setUser_id_receiver(rentRepository.idUserByRent(roomId));
                notificationApp.setContent("Rent successfully room " + updatedRent.getId());
                Date currentTime = new Date();
                notificationApp.setTime(new Date(currentTime.getTime()));
                notificationRepository.save(notificationApp);
                return "Room confirmation successful!";
            } else {
                return "Room confirmation failed.";
            }
        } else {
            return "Room ID not found.";
        }
    }

    @Override
    public String AddUserInRoomMobile(long roomId,long rentId) {
        Optional<Rent> updatedRentOptional = rentRepository.findById(rentId);
        if (updatedRentOptional.isPresent()) {
           Integer checkUpdateRent = rentRepository.updateRentStatusByRoomIdMobile(rentId);
            Integer checkUpdateRoom =  roomRepository.updateRoomStatusById(roomId);
           if (checkUpdateRent > 0 && checkUpdateRoom > 0){
               Rent updatedRent = updatedRentOptional.get();
               NotificationApp notificationApp = new NotificationApp();
               notificationApp.setUser_id_sender(rentRepository.idHostByRent(roomId));
               notificationApp.setUser_id_receiver(rentRepository.idUserByRent(roomId));
               notificationApp.setContent("Rent successfully room " + updatedRent.getId());
               Date currentTime = new Date();
               notificationApp.setTime(new Date(currentTime.getTime()));
               notificationRepository.save(notificationApp);
               return "Room confirmation successful!";
           }else {
               return "update failed.";
           }

        } else {
            return "Rent ID not found.";
        }
    }
    @Override
    public String CancelUserInRoomMobile(long roomId,long rentId) {
        Optional<Rent> updatedRentOptional = rentRepository.findById(rentId);
        if (updatedRentOptional.isPresent()) {
            Integer checkUpdateRent = rentRepository.cancelRentStatusByRoomIdMobile(rentId);
            if (checkUpdateRent > 0 ){
                Rent updatedRent = updatedRentOptional.get();
                NotificationApp notificationApp = new NotificationApp();
                notificationApp.setUser_id_sender(rentRepository.idHostByRent(roomId));
                notificationApp.setUser_id_receiver(rentRepository.idUserByRent(roomId));
                notificationApp.setContent("Rent fail room " + updatedRent.getId());
                Date currentTime = new Date();
                notificationApp.setTime(new Date(currentTime.getTime()));
                notificationRepository.save(notificationApp);
                return "Room cancel successful!";
            }else {
                return "update failed.";
            }

        } else {
            return "Rent ID not found.";
        }
    }


    @Override
    public List<Rent> getAllRentConfirm(long hostId) {
        return rentRepository.findRentByHostId(hostId);
    }

    @Override
    public List<Rent> getAllUser(long hostId) {
        return rentRepository.findUsersByHostId(hostId);
    }

    @Override
    public List<Room> getAllRoomByHost(long hostId) {
        //return roomRepository.allRoomsEmpty(hostId);
        List<Room> emptyRooms = roomRepository.allRoomsEmpty(hostId);
        for (Room room : emptyRooms) {
            String filePath = room.getImg();
            room.setImg(filePath);
        }
        return emptyRooms;
    }
    @Override
    public List<Room> AllRoom(long hostId) {
        return roomRepository.allRoomsNe(hostId);
    }

    @Override
    public List<ListandCoutRoom>  RoomEmptyByBoarding( long HostId) {
        return roomRepository.getRoomCountsForHost(HostId);
    }

    @Override
    public List<Boarding_host> getAllBoardingHostelConfirm(long hostId) {
        return boardingRepository.allRoomsOk(hostId);
    }

    @Override
    public List<Boarding_host> getAllBoardingByUser(long hostId) {
        return boardingRepository.allBoardingByHost(hostId);
    }

    @Override
    public int countRoomEmpty(long BoardingId, long HostId) {
        Boarding_host boardingHostel = getBoardingIfExists(BoardingId, HostId);
        return roomRepository.countRoomEmpty(BoardingId, HostId);

    }

    @Override
    public int countRoomEmptyReal(long HostId) {
        return roomRepository.countRoomEmptyReal(HostId);
    }
    @Override
    public int contRoom(long HostId) {
        return roomRepository.countRoom(HostId);
    }
    @Override
    public int contRent(long HostId) {
        return rentRepository.peopleRent(HostId);
    }

    @Override
    public List<Rent> getUserInRent(long HostId) {
        return rentRepository.getUserInRent(HostId);
    }

    public Map uploadImage(MultipartFile file) throws IOException {
        // String publicId = "Hostel/" + UUID.randomUUID().toString() + System.currentTimeMillis();
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String publicId = "Hostel/" + originalFilename;
        Map params = ObjectUtils.asMap(
                "public_id", publicId,
                "folder", "Hostel"
        );
        Map result = cloudinary.uploader().upload(file.getBytes(), params);
        return result;
    }

    @Override
    public ImgRoom addImgRoom(long idRoom, long hostId, MultipartFile imageFile1, MultipartFile imageFile2, MultipartFile
            imageFile3, MultipartFile imageFile4, MultipartFile imageFile5) {
        Users host = getUserWithRole(hostId, 2);
        Room room = getRoomIfExists(idRoom);
        ImgRoom imgRoom = new ImgRoom();
        List<MultipartFile> validImageFiles = Arrays.asList(imageFile1, imageFile2, imageFile3, imageFile4, imageFile5).stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        try {
            List<Map> uploadResults = uploadListImages(validImageFiles);

            for (int i = 0; i < validImageFiles.size(); i++) {
                String imageUrl = uploadResults.get(i).get("secure_url").toString();
                setImgUrl(imgRoom, i + 1, imageUrl);
            }

            imgRoom.setRoom(room);
            ImgRoom savedRoom = listImgRepository.save(imgRoom);

            return savedRoom;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error uploading images.", e);
        }
    }

    private void setImgUrl(ImgRoom imgRoom, int index, String imageUrl) {
        switch (index) {
            case 1:
                imgRoom.setImgUrls1(imageUrl);
                break;
            case 2:
                imgRoom.setImgUrls2(imageUrl);
                break;
            case 3:
                imgRoom.setImgUrls3(imageUrl);
                break;
            case 4:
                imgRoom.setImgUrls4(imageUrl);
                break;
            case 5:
                imgRoom.setImgUrls5(imageUrl);
                break;
            default:
                throw new IllegalArgumentException("Invalid image index: " + index);
        }
    }

    private Users getUserWithRole(long userId, int roleId) {
        Optional<Users> usersOptional = userRepository.findById(userId);
        if (!usersOptional.isPresent() || usersOptional.get().getRole().getId() != roleId) {
            throw new RuntimeException("Người dùng không có vai trò cần thiết để thêm phòng.");
        } else if (!usersOptional.get().getConfirmation_status().equals("confirm") && !usersOptional.get().getConfirmation_status().equals("ban")) {
            throw new RuntimeException("Người dùng chưa được xác nhận.");
        }
        return usersOptional.get();
    }

    private ResponseAll checkBan(long userId) {
        Optional<Users> usersOptional = userRepository.findById(userId);
        if (usersOptional.get().getConfirmation_status().equals("ban")) {
            return new ResponseAll(false, "Your account has been locked for 30 days");
        }
        return new ResponseAll(true, "OK");
    }

    private Room getRoomIfExists(long roomId) {
        Optional<Room> roomOptional = roomRepository.findById(roomId);
        if (!roomOptional.isPresent()) {
            throw new RuntimeException("Phòng không tồn tại.");
        }
        return roomOptional.get();
    }

    private Boarding_host getBoardingIfExists(long boardingId, long userId) {
        Optional<Boarding_host> boardingHost = boardingRepository.findIdAndHost(userId, boardingId);
        if (!boardingHost.isPresent()) {
            throw new RuntimeException("Boarding Host Not Found");
        }
        return boardingHost.get();
    }

    private List<Map> uploadListImages(List<MultipartFile> files) throws IOException {
        List<Map> uploadResults = new ArrayList<>();

        if (files == null) {
            return uploadResults;
        }

        for (MultipartFile file : files) {
            if (file == null) {
                continue;
            }

//            String contentType = file.getContentType();
//            if (!isValidImageFormat(contentType)) {
//                throw new IllegalArgumentException("Định dạng ảnh không được hỗ trợ: " + contentType);
//            }

            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String publicId = "ImgRoom/" + originalFilename;
            Map<String, String> params = ObjectUtils.asMap(
                    "public_id", publicId,
                    "folder", "Hostel"
            );

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), params);
            uploadResults.add(uploadResult);
        }

        return uploadResults;
    }


    private boolean isValidImageFormat(String contentType) {
        return contentType != null && contentType.startsWith("image/");
    }
}
