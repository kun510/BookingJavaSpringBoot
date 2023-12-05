package com.example.doancuoiky.hostel.controller;


import com.example.doancuoiky.hostel.model.Boarding_host;
import com.example.doancuoiky.hostel.model.Report;
import com.example.doancuoiky.hostel.model.Users;
import com.example.doancuoiky.hostel.response.ResponseAll;
import com.example.doancuoiky.hostel.response.ResponseOtp;
import com.example.doancuoiky.hostel.service.IadminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/Admin")
public class AdminController {
    @Autowired
    private IadminService iadminService;

    @GetMapping("/getConfirm")
    public Object  getBoardingWait() {
        List<Boarding_host> boardingHostList = iadminService.getAllBoardingHostelWaiting();
        if (boardingHostList.isEmpty()){
            return "There is no waiting Boarding for approval";
        }
        return boardingHostList;
    }
    @GetMapping("/getAllUser")
    public Object getUser() {
        List<Users> usersList = iadminService.user();
        return usersList;
    }
    @GetMapping("/getReport1")
    public Object getReport1() {
        List<Report> reports1 = iadminService.ListReportTopTime1();
        return reports1;
    }
    @GetMapping("/getReport2")
    public Object getReport2() {
        List<Report> reports2 = iadminService.ListReportTopTime2();
        return reports2;
    }
    @GetMapping("/getReport3")
    public Object getReport3() {
        List<Report> reports3 = iadminService.ListReportTopTime3();
        return reports3;
    }
    @GetMapping("/getAllHost")
    public Object  getHost() {
        List<Users> usersList = iadminService.host();
        return usersList;
    }
    @GetMapping("/getHostWait")
    public Object  getHostWait() {
        List<Users> usersList = iadminService.WaitHost();
        if (usersList == null){
            return "Not Host accept";
        }
        return usersList;
    }
    @PutMapping("/ConfirmBoarding")
    public ResponseEntity<ResponseAll> updateBoardingStatus(@RequestParam("boardingId") long boardingId,@RequestParam("adminId") long AdminId) {
        ResponseAll response = iadminService.UpdateBoardingStatus(AdminId,boardingId);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
    @PutMapping("/ConfirmHost")
    public ResponseEntity<ResponseAll> updateHostStatus(@RequestParam("adminId") long adminId,@RequestParam("hostId") long hostId) {
        ResponseAll response = iadminService.UpdateHostStatus(adminId,hostId);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
    @GetMapping("/countUser")
    public int countUser(){
       return iadminService.countUser();
    }
    @GetMapping("/countHost")
    public int countHost(){
        return iadminService.countHost();
    }
    @GetMapping("/countRoom")
    public int countRoom(){
        return iadminService.countRoom();
    }
    @GetMapping("/countReport")
    public int countReport(){
        return iadminService.countReport();
    }
    @GetMapping("/listBan")
    public List<Users> ListBan(){
        return iadminService.ListBan();
    }

    @PutMapping("/banHost")
    public ResponseEntity<ResponseAll> banHost(@RequestParam("adminId") long adminId,@RequestParam("hostId") long hostId) {
        ResponseAll response = iadminService.Ban(adminId,hostId);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
    @PutMapping("/unBanHost")
    public ResponseEntity<ResponseAll> unBanHost(@RequestParam("adminId") long adminId,@RequestParam("hostId") long hostId) {
        ResponseAll response = iadminService.unBan(adminId,hostId);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/CancelHost")
    public ResponseEntity<ResponseAll> CancelHost(@RequestParam("adminId") long adminId,@RequestParam("hostId") long hostId) {
        ResponseAll response = iadminService.UpdateCancelHostStatus(adminId,hostId);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
    @PutMapping("/CancelBoarding")
    public ResponseEntity<ResponseAll> CancelBoarding(@RequestParam("adminId") long adminId,@RequestParam("boardingId") long boardingId) {
        ResponseAll response = iadminService.UpdateCancelBoardingStatus(adminId,boardingId);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/sendMail")
    public ResponseOtp sendMailChangePassword(@RequestParam("sendTo")  String to) {
        ResponseOtp responseOtp = iadminService.sendMailChangePassword(to);
        if (responseOtp.isSuccess()){
            return responseOtp;
        }
        return new ResponseOtp(false,"fail Send Mail");
    }


}
