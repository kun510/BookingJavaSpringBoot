package com.example.doancuoiky.hostel.model;

import javax.persistence.*;

@Entity
@Table(name = "RoomFavourite")
public class RoomFavourite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

}
