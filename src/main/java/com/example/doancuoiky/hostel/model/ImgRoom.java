package com.example.doancuoiky.hostel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ImgRoom")
@Data
public class ImgRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "img_room_id")
    private Long id;
    @Column(name = "url1")
    private String imgUrls1;
    @Column(name = "url2")
    private String imgUrls2;
    @Column(name = "url3")
    private String imgUrls3;
    @Column(name = "url4")
    private String imgUrls4;
    @Column(name = "url5")
    private String imgUrls5;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

}

