package com.example.bbapp.service;

import com.example.bbapp.dto.AppointmentDto;
import com.example.bbapp.dto.BarberDto;

import java.time.LocalDate;
import java.util.List;

public interface BarberService {
    List<BarberDto> getAllBarbers();
    void deleteBarber(Long barberId);
    BarberDto getBarberByUsernameOrPhone(String usernameOrPhone);
    List<AppointmentDto> getBarberAppointments(String username);
    List<AppointmentDto> getBarberAppointmentsByDate(String usernameOrPhone, LocalDate date);
    public void closeDay(String username, LocalDate date);

    /** NOT USED
    //BarberDto createBarber(BarberDto barberDto); // Add barber
     **/

}
