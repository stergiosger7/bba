package com.example.bbapp.dto;


import com.example.bbapp.entity.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto {
    private Long appointmentId;
    private String customerFirstName;
    private String customerLastName;
    private String customerPhone;
    private String barberName;
    private LocalDateTime appointmentTime;
    private String serviceName;
    private BigDecimal servicePrice;
    private AppointmentStatus status;
    private String notes;
}
