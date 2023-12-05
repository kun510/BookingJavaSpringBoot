package com.example.doancuoiky.hostel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
@Entity
@Data
@Table(name = "Review_boarding")
public class ReviewBoarding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "evaluate")
    private String evaluate;

    @Column(name = "number_of_stars")
    private float numberOfStars;

    @ManyToOne
    @JoinColumn(name = "id_user")
    @JsonIgnore
    private Users user;

    @ManyToOne()
    @JoinColumn(name = "boarding_id")
    private Boarding_host boarding;

    @Column(name = "day")
    private Date date;
}
