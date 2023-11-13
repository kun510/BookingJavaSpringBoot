package com.example.doancuoiky.hostel.controller;


import com.example.doancuoiky.hostel.model.Boarding_host;
import com.example.doancuoiky.hostel.model.Users;
import com.example.doancuoiky.hostel.response.ResponseAll;
import com.example.doancuoiky.hostel.service.IadminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public Object getUser(@RequestParam("idAdmin") long adminId) {
        List<Users> usersList = iadminService.user(adminId);

        return usersList;
    }
    @GetMapping("/getAllHost")
    public Object  getHost(@RequestParam("idAdmin") long adminId) {
        List<Users> usersList = iadminService.host(adminId);
        return usersList;
    }
    @GetMapping("/getHostWait")
    public Object  getHostWait(@RequestParam("idAdmin") long adminId) {
        List<Users> usersList = iadminService.WaitHost(adminId);
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
    //làm report 1 lần thông báo nhắc nhỡ,2 lần hạn chế đăng trọ 2 ngày,3 lần xoá tài khoản
    //yêu cầu gỡ báo cáo , 1 gửi hình ảnh chứng minh, 2 user báo cáo gửi xác nhận gỡ báo cáo
}
