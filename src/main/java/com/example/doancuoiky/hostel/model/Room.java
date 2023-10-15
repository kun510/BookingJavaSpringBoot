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

    @Column(name = "address")
    private String address;

    @Column(name = "area")
    private String area;

    @Column(name = "description")
    private String description;

    @Column(name = "img")
    private String img;
//    @Lob
//    @Column(name = "img")
//    private byte[] img;

    @Column(name = "number_stars")
    private float numberOfStars;

    @Column(name = "number_room")
    private int numberRoom;

    @Column(name = "status")
    private String status;

    @Column(name = "utility_bills")
    private String utilityBills;

    @Column(name = "price")
    private int price;

    @Column(name = "people")
    private String people;

    @Column(name = "type")
    private String type;
    @ManyToOne
    @JoinColumn(name = "id_host")
    @JsonIgnore
    private Users user;

}
