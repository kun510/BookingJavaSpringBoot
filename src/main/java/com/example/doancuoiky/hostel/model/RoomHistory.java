package com.example.doancuoiky.hostel.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "RoomHistory")
public class RoomHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "date_of_hire")
    private Date dateOfHire;

    @Column(name = "move_in")
    private Date moveIn;

    @Column(name = "move_out")
    private Date moveOut;

}
