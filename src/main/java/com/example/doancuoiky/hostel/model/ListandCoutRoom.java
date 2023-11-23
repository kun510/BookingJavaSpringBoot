package com.example.doancuoiky.hostel.model;

import lombok.Data;

import java.util.List;

@Data
public class ListandCoutRoom {
    public Boarding_host boardingHost;
    public long count;


    public ListandCoutRoom(Boarding_host id_boarding, long count) {
        this.boardingHost = id_boarding;
        this.count = count;

    }


}
