package com.example.doancuoiky.hostel.request;


import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.util.Date;
@Data
public class ReportRq {
    @NotBlank(message = "Reason is required")
    private String reason ;

}
