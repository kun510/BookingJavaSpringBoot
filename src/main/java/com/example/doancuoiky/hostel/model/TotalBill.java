package com.example.doancuoiky.hostel.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "TotalBill")
public class TotalBill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "electric_bill")
    private int electricBill;
    @Column(name = "month")
    private String month;
    @Column(name = "sum")
    private int sum;
    @Column(name = "water_bill")
    private int waterBill;
    @Column(name = "costs_incurred")
    private int CostsIncurred;
    @ManyToOne
    @JoinColumn(name = "rent_id")
    private Rent rent;

}
