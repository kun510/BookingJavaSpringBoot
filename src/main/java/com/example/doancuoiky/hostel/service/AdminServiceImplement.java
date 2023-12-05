package com.example.doancuoiky.hostel.service;

import com.example.doancuoiky.hostel.model.Boarding_host;
import com.example.doancuoiky.hostel.model.NotificationApp;
import com.example.doancuoiky.hostel.model.Report;
import com.example.doancuoiky.hostel.model.Users;
import com.example.doancuoiky.hostel.repository.*;
import com.example.doancuoiky.hostel.response.ResponseAll;
import com.example.doancuoiky.hostel.response.ResponseOtp;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AdminServiceImplement implements IadminService{

    @Value("${dialogflow.project-id}")
    private String projectId;

    @Value("${dialogflow.credentials-file}")
    private String credentialsFilePath;

    @Value("${spring.mail.username}")
    private String fromEmail;
    @Autowired
    private BoardingRepository boardingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private JavaMailSender mailSender;
    @Override
    public List<Users> user() {
        return userRepository.allUsers();
    }

    @Override
    public List<Users> host() {
        return userRepository.allHost();
    }

    @Override
    public List<Users> WaitHost() {
        return userRepository.allUsersWait();
    }

    @Override
    public List<Boarding_host> getAllBoardingHostelWaiting() {
        return boardingRepository.allRoomsWaiting();
    }

    @Override
    public ResponseAll UpdateBoardingStatus(long AdminId, long boardingId) {
            boolean isAdmin = checkAdmin(AdminId);
            if (isAdmin){
                Boarding_host boardingHostel = getBoardingIfExists(boardingId);
                boardingRepository.updateBoardingStatusById(boardingId);
               Users admin = userRepository.checkAdmin(AdminId);
               Users host = boardingRepository.findHostById(boardingId);
                NotificationApp notificationApp = new NotificationApp();
                notificationApp.setUser_id_sender(admin);
                notificationApp.setUser_id_receiver(host);
                notificationApp.setContent("Your Boarding confirmed by Admin");
                Date currentTime = new Date();
                notificationApp.setTime(new Date(currentTime.getTime()));
                notificationRepository.save(notificationApp);
                return new ResponseAll(true, "Boarding status updated successfully");
            }else {
                return new ResponseAll(false,"User is not a Admin.");
            }
    }

    @Override
    public ResponseAll UpdateHostStatus(long AdminId, long UserId) {
        boolean isAdmin = checkAdmin(AdminId);
        boolean isHost = checkHost(UserId);
        if (isAdmin){
            if (isHost){
                Users admin = userRepository.checkAdmin(AdminId);
                Users host = userRepository.checkHost(UserId);
                NotificationApp notificationApp = new NotificationApp();
                notificationApp.setUser_id_sender(admin);
                notificationApp.setUser_id_receiver(host);
                notificationApp.setContent("Your account confirmed by Admin");
                userRepository.updateUserStatusById(UserId);
                Date currentTime = new Date();
                notificationApp.setTime(new Date(currentTime.getTime()));
                notificationRepository.save(notificationApp);
                return new ResponseAll(true,"Update Successfully");
            }else {
                return new ResponseAll(false,"not Found");
            }

        }else {
            return new ResponseAll(false,"Cannot update status because the user is not a Admin.");
        }
    }

    private Boarding_host getBoardingIfExists(long boardingId) {
        Optional<Boarding_host> boardingHost = boardingRepository.findById(boardingId);
        if (!boardingHost.isPresent()) {
            throw new RuntimeException("Boarding Host Not Found");
        }
        return boardingHost.get();
    }

    boolean checkAdmin(long AdminId) {
        Users users = userRepository.findByIdAndRoleEqualsOne(AdminId);
        return users != null;
    }
    boolean checkHost(long Host){
        Users users = userRepository.checkHost(Host);
        return users != null;
    }
    @Override
    public int countUser() {
        return userRepository.countUser();
    }

    @Override
    public int countHost() {
        return userRepository.countHost();
    }

    @Override
    public int countRoom() {
        return roomRepository.countRoom() ;
    }

    @Override
    public int countReport() {
        return reportRepository.countReport();
    }

    @Override
    public List<Report> ListReportTopTime1() {
        return reportRepository.ListReportTopTime1();
    }

    @Override
    public List<Report> ListReportTopTime2() {
        return reportRepository.ListReportTopTime2();
    }

    @Override
    public List<Report> ListReportTopTime3() {
        return reportRepository.ListReportTopTime3();
    }

    @Override
    public ResponseAll Ban(long AdminId,long HostId) {
        boolean isAdmin = checkAdmin(AdminId);
        if (isAdmin){
            userRepository.banUserStatusById(HostId);
            reportRepository.deleteReportByHostId(HostId);
            return new ResponseAll (true,"ban Successfully");
        }
        return new ResponseAll (false,"you don't Admin");
    }
    @Override
    public ResponseAll unBan(long AdminId,long HostId) {
        boolean isAdmin = checkAdmin(AdminId);
        if (isAdmin){
            userRepository.updateUserStatusById(HostId);
            return new ResponseAll (true,"unBan Successfully");
        }
        return new ResponseAll (false,"you don't Admin");
    }

    @Override
    public List<Users> ListBan() {
        return userRepository.listBan();
    }

    @Override
    public ResponseAll UpdateCancelBoardingStatus(long AdminId, long boardingId) {
        boolean isAdmin = checkAdmin(AdminId);
        if (isAdmin){
            boardingRepository.updateCancelBoardingStatusById(boardingId);
            return new ResponseAll (true,"Cancel Successfully");
        }
        return new ResponseAll (false,"you don't Admin");
    }

    @Override
    public ResponseAll UpdateCancelHostStatus(long AdminId, long UserId) {
        boolean isAdmin = checkAdmin(AdminId);
        if (isAdmin){
            userRepository.updateUserCancelStatusById(UserId);
            return new ResponseAll (true,"Cancel Successfully");
        }
        return new ResponseAll (false,"you don't Admin");
    }

    @Override
    public ResponseOtp sendMailChangePassword(String to) {
        try {
            String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            Random random = new Random();
            StringBuilder randomString = new StringBuilder();
            for (int i = 0; i < 3; i++) {
                int randomIndex = random.nextInt(CHARACTERS.length() - 10);
                randomString.append(CHARACTERS.charAt(randomIndex));
            }
            for (int i = 0; i < 3; i++) {
                int randomIndex = random.nextInt(10) + 52;
                randomString.append(CHARACTERS.charAt(randomIndex));
            }
            String bodyWithRandomString = "Mã của bạn là: <strong>" + randomString + "</strong>" +
                    " Hãy điền đúng vào ứng dụng để thay đổi mật khẩu";
            String title = "Mã xác thực yêu cầu thay đổi mật khẩu";
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(title);
            mimeMessageHelper.setText(bodyWithRandomString, true);

            mailSender.send(mimeMessage);
            return new ResponseOtp(true,randomString.toString());

        } catch (Exception e) {
            return new ResponseOtp(false,"lỗi sendMail");
        }
    }


}
