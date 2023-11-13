package com.example.doancuoiky.hostel.request;

import lombok.Data;

import javax.persistence.Column;

@Data
public class AddRoom {
    private String description;
    private int numberRoom;
    private int ElectricBill;
    private int WaterBill;
    private int price;
    private String people;
    private String type;
}
