package com.example.doancuoiky.hostel.repository;

import com.example.doancuoiky.hostel.model.FileData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileDataRepository extends JpaRepository<FileData,Integer> {
    List<FileData> findAllByName(String fileName);
}
