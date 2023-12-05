package com.example.doancuoiky.hostel.model;


import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "Users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "phone",nullable = false)
    private String phone;
    @Column(name = "password",nullable = false)
    private String password;
    @Column(name = "Address")
    private String address;
    @Column(name = "email")
    private String email;
    @Column(name = "img")
    private String img;
    @Column(name = "name")
    private String name;
    @Column(name = "confirmation_status")
    private String confirmation_status;
    @Column(name = "token_Device")
    private String token_device;
    @ManyToOne(optional = true)
    @JoinColumn(name = "id_role")
    private Role role;
}

