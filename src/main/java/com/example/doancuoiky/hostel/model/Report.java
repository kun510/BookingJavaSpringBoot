package com.example.doancuoiky.hostel.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "Report")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "day")
    private Date day;
    @Column(name = "reason")
    private String reason;
    @Column(name = "img1")
    private String img1;
    @Column(name = "img2")
    private String img2;
    @Column(name = "img3")
    private String img3;
    @Column(name = "times_report")
    private int times_report;
    @Column(name = "status")
    private String status;
    @Column(name = "unban")
    private Date unban;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

}
