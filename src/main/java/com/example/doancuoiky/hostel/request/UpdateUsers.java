package com.example.doancuoiky.hostel.request;

import com.example.doancuoiky.hostel.model.Role;
import com.example.doancuoiky.hostel.model.Room;
import lombok.Data;

import javax.persistence.*;

@Data
public class UpdateUsers {
    private String address;
    private String email;
    private String name;
}
