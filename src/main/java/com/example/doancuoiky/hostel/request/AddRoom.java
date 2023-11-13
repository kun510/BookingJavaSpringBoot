package com.example.doancuoiky.hostel.request;

import lombok.Data;

import javax.persistence.Column;

@Data
public class AddRoom {
    private String address;
    private String area;
    private String description;
    private String img;
    private int numberRoom;
    private String status;
    private String utilityBills;
    private int price;
    private String people;
    private String type;
}
