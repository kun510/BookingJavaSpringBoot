package com.example.doancuoiky.hostel.model;

import lombok.Data;

@Data
public class RoomWithImagee {
    private int position; // Định danh động để xác định thứ tự hình ảnh
    private byte[] imageData;

}
