package com.example.bbapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BarberDto {
    private Long barberId;
    private String barberName;
    private String phone;
    private String username;
    private LocalDateTime createdAt;
    private Set<String> roles;
}
