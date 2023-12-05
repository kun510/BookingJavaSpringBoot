package com.example.doancuoiky.hostel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "Boarding_host")
public class Boarding_host {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "address")
    private String address;
    @Column(name = "area")
    private String area;
    @Column(name = "img")
    private String img;
    @Column(name = "status")
    private String status;
    @Column(name = "number_room")
    private int numberRoom;
    @Column(name = "number_stars")
    private float numberOfStars;
    @ManyToOne
    @JoinColumn(name = "id_host")
    private Users user;
}
