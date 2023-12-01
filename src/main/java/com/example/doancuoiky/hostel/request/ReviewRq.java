package com.example.doancuoiky.hostel.request;

import com.example.doancuoiky.hostel.model.Room;
import com.example.doancuoiky.hostel.model.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
public class ReviewRq {
    private String evaluate;
    private float numberOfStars;
}
