package com.example.bbapp.service.impl;

import com.example.bbapp.dto.AppointmentDto;
import com.example.bbapp.dto.BarberDto;
import com.example.bbapp.entity.Appointment;
import com.example.bbapp.entity.Barber;
import com.example.bbapp.entity.Holiday;
import com.example.bbapp.exception.BarberAPIException;
import com.example.bbapp.mapper.AppointmentMapper;
import com.example.bbapp.mapper.BarberMapper;
import com.example.bbapp.repository.AppointmentRepository;
import com.example.bbapp.repository.BarberRepository;
import com.example.bbapp.repository.HolidayRepository;
import com.example.bbapp.service.BarberService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BarberServiceImpl implements BarberService {

    private BarberRepository barberRepository;
    private AppointmentRepository appointmentRepository;
    private HolidayRepository holidayRepository;

    @Override
    public List<BarberDto> getAllBarbers() {
        List<Barber> barbers = barberRepository.findAll();
        return barbers
                .stream()
                .map((barber) -> BarberMapper.toBarberDto(barber))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBarber(Long barberId) {
        //delete barber
        Barber barber = barberRepository.findById(barberId)
                .orElseThrow(()-> new EntityNotFoundException("Barber with id: " + barberId + " not found"));


        // 1. Clear the roles relationship (removes entries from barber_role table ONLY)
        barber.getRoles().clear();

        // 3. Save to persist the relationship changes
        barberRepository.save(barber);

        barberRepository.delete(barber);
    }

    @Override
    public BarberDto getBarberByUsernameOrPhone(String usernameOrPhone) {
        Barber barber = barberRepository.findByUsernameOrPhone(usernameOrPhone,usernameOrPhone)
                .orElseThrow(() -> new BarberAPIException(
                        HttpStatus.NOT_FOUND,
                        "Barber not found with username or phone: " + usernameOrPhone
                ));
        return BarberMapper.toBarberDto(barber);
    }

    @Override
    public List<AppointmentDto> getBarberAppointments(String usernameOrPhone) {

        Barber barber = barberRepository.findByUsernameOrPhone(usernameOrPhone,usernameOrPhone)
                .orElseThrow(() -> new BarberAPIException(
                        HttpStatus.NOT_FOUND,
                        "Barber not found with username or phone: " + usernameOrPhone
                ));

        List<Appointment> appointments = appointmentRepository.findAppointmentsByBarberId(barber.getBarberId());

        return appointments.stream()
                .map((appointment) -> AppointmentMapper.toAppointmentDto(appointment))
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDto> getBarberAppointmentsByDate(String usernameOrPhone, LocalDate date) {

        Barber barber = barberRepository.findByUsernameOrPhone(usernameOrPhone,usernameOrPhone)
                .orElseThrow(() -> new BarberAPIException(
                        HttpStatus.NOT_FOUND,
                        "Barber not found with username or phone: " + usernameOrPhone
                ));

        List<Appointment> appointments = appointmentRepository.findAppointmentsByExactDate(date, barber.getBarberId());

        return appointments.stream()
                .map((appointment) -> AppointmentMapper.toAppointmentDto(appointment) )
                .collect(Collectors.toList());
    }

    @Override
    public void closeDay(String usernameOrPhone, LocalDate date) {

        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot close past dates");
        }

        Barber barber = barberRepository.findByUsernameOrPhone(usernameOrPhone,usernameOrPhone)
                .orElseThrow(() -> new BarberAPIException(
                        HttpStatus.NOT_FOUND,
                        "Barber not found with username or phone: " + usernameOrPhone
                ));

        if (!holidayRepository.existsByMonthAndDayAndBarberId(date.getMonthValue(),
                date.getDayOfMonth(),
                barber.getBarberId())) {
            holidayRepository.save(
                    Holiday.builder()
                            .month(date.getMonthValue())
                            .day(date.getDayOfMonth())
                            .barberId(barber.getBarberId())
                            .name("Day off")
                            .build());
            }
    }

    /**
    //add barber... WITH REGISTER
    @Override
    public BarberDto createBarber(BarberDto barberDto) {

        //convert dto to entity
        Barber barber = BarberMapper.toBarberEntity(barberDto);
        barber.setPasswordHash("test");
        Barber createdBarber = barberRepository.save(barber);

        return BarberMapper.toBarberDto(createdBarber);
    }
    **/
}
