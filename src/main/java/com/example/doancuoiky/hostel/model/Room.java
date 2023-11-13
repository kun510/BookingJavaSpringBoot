package com.example.doancuoiky.hostel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.util.List;
import javax.persistence.*;
@Entity
@Data
@Table(name = "Rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "img")
    private String img;

    @Column(name = "number_stars")
    private float numberOfStars;

    @Column(name = "number_room")
    private int numberRoom;

    @Column(name = "status")
    private String status;

    @Column(name = "ElectricBill")
    private int ElectricBill;

    @Column(name = "WaterBill")
    private int WaterBill;

    @Column(name = "price")
    private int price;

    @Column(name = "people")
    private String people;

    @Column(name = "type")
    private String type;
    @ManyToOne
    @JoinColumn(name = "id_host")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "id_boarding")
    private Boarding_host boardingHostel;

}
