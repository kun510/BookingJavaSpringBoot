package com.example.doancuoiky.hostel.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "Rent")
public class Rent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;
    @Column(name = "status")
    private String status;
    @Column(name = "peopleInRoom")
    private int peopleInRoom;
}
