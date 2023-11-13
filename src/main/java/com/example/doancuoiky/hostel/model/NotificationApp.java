package com.example.doancuoiky.hostel.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

@Data
@Entity
@Table(name = "Notification")
public class NotificationApp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id_sender")
    private Users user_id_sender;

    @Column(name = "content")
    private String content;

    @Column(name = "time")
    private Date time;

    @ManyToOne
    @JoinColumn(name = "user_id_receiver")
    private Users user_id_receiver;
}

