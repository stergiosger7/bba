package com.example.bbapp.service;

import com.example.bbapp.dto.AppointmentDto;
import com.example.bbapp.dto.BarberDto;
import com.example.bbapp.dto.CreateAppointmentDto;
import com.example.bbapp.entity.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

public interface AppointmentService {
    AppointmentDto createAppointment(CreateAppointmentDto createAppointmentDTO);
    AppointmentDto updateAppointment(Long appointmentId, AppointmentDto appointmentDto, String username);
    void cancelAppointment(Long appointmentId);
    List<LocalTime> getAvailableSlots(Long barberId, LocalDate date);
    boolean isSlotAvailable(Long barberId, LocalDateTime appointmentTime);
    void confirmAppointment(Long appointmentId, String username);
    public List<String> getAvailableDates(Long barberId, YearMonth month);
}
