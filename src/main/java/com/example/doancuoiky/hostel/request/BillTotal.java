package com.example.doancuoiky.hostel.request;

import lombok.Data;

import java.util.Date;

@Data
public class BillTotal {
    private int electricBill;
    private Date month;
    private int waterBill;
    private int CostsIncurred;
}
