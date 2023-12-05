package com.example.doancuoiky.hostel.request;

import lombok.Data;

@Data
public class RegisterRq {
    private String phone;
    private String password;
    private String address;
    private String email;
    private String img;
    private String name;
}
