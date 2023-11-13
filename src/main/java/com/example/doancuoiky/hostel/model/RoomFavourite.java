package com.example.doancuoiky.hostel.model;

import com.google.api.client.util.DateTime;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
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
    @Column(name = "day")
    private Date day;
}
