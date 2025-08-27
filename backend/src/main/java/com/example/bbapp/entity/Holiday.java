package com.example.bbapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Holiday {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private int month;
    private int day;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long barberId;

}
