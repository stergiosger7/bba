package com.example.bbapp.controller;


import com.example.bbapp.dto.AppointmentDto;
import com.example.bbapp.dto.BarberDto;
import com.example.bbapp.dto.CreateAppointmentDto;
import com.example.bbapp.entity.AppointmentStatus;
import com.example.bbapp.service.AppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class AppointmentController {

    private AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<AppointmentDto> createAppointment(@RequestBody CreateAppointmentDto createAppointmentDto){
        AppointmentDto createdAppointment = appointmentService.createAppointment(createAppointmentDto);

        return new ResponseEntity<>(createdAppointment,HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_BARBER')")
    @PutMapping("{id}/update")
    public ResponseEntity<AppointmentDto> updateAppointment(@PathVariable("id") Long appointmentId,@RequestBody AppointmentDto appointmentDto
            , Authentication authentication){

        String usernameOrPhone = authentication.getName();

        AppointmentDto updatedAppointment = appointmentService.updateAppointment(appointmentId,appointmentDto,usernameOrPhone);

        return ResponseEntity.ok(updatedAppointment);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_BARBER')")
    @DeleteMapping("{id}")
    // For deleting the appointment
    public ResponseEntity<String> cancelAppointment(@PathVariable("id") Long appointmentId){
        appointmentService.cancelAppointment(appointmentId);
        return ResponseEntity.ok("Appointment cancelled successfully!");
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_BARBER')")
    @PatchMapping("{id}/confirm")
    public ResponseEntity<String> confirmAppointment(@PathVariable("id") Long appointmentId, Authentication authentication) {
        String usernameOrPhone = authentication.getName();

        appointmentService.confirmAppointment(appointmentId,usernameOrPhone);

        return ResponseEntity.ok("Appointment confirmed!");
    }

    @GetMapping("/available-dates")
    public ResponseEntity<List<String>> getAvailableDates(
            @RequestParam Long barberId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month
    ) {
        List<String> availableDates = appointmentService.getAvailableDates(barberId, month);
        return ResponseEntity.ok(availableDates);
    }

    @GetMapping("/available-slots")
    public ResponseEntity<List<LocalTime>> getAvailableSlots(
            @RequestParam Long barberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<LocalTime> slots = appointmentService.getAvailableSlots(barberId, date);
        return ResponseEntity.ok(slots);
    }

}
