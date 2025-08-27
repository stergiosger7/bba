package com.example.bbapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "service")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceId;

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @Column(name = "price", precision = 6, scale = 2)
    private BigDecimal price;

    @Column(name = "duration")
    private Integer duration = 30; // minutes
}
