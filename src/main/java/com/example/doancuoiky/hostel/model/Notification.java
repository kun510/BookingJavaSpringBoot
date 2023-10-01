package com.example.doancuoiky.hostel.model;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Table(name = "Notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "content")
    private String content;

    @Column(name = "time")
    private Time time;

}

