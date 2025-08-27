package com.example.bbapp.repository;

import com.example.bbapp.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface HolidayRepository extends JpaRepository<Holiday,Long> {
    // Check if a specific barber closed a date
    boolean existsByMonthAndDayAndBarberId(int month, int day, Long barberId);


    boolean existsByMonthAndDay(int month, int day);
}
