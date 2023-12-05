package com.example.doancuoiky.hostel.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "Help")
public class Help {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "img1")
    private String img1;
    @Column(name = "img2")
    private String img2;
    @Column(name = "img3")
    private String img3;
    @Column(name = "content_help")
    private String contentHelp;
    @ManyToOne
    @JoinColumn(name = "id_host")
    private Users user;
}
