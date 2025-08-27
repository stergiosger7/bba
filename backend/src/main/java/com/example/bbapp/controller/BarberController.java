package com.example.bbapp.controller;


import com.example.bbapp.dto.AppointmentDto;
import com.example.bbapp.dto.BarberDto;
import com.example.bbapp.service.BarberService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/barbers")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class BarberController {

    private BarberService barberService;

    @GetMapping("/all")
    public ResponseEntity<List<BarberDto>> getAllBarbers() {
        List<BarberDto> barbers = barberService.getAllBarbers();
        return ResponseEntity.ok(barbers);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("{id}")
    // For deleting the barber
    public ResponseEntity<String> deleteBarber(@PathVariable("id") Long barberId){
        barberService.deleteBarber(barberId);
        return ResponseEntity.ok("Barber deleted successfully!");
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_BARBER')")
    @GetMapping("/me")
    public ResponseEntity<BarberDto> getBarberByUsernameOrPhone(Authentication authentication) {
        String usernameOrPhone = authentication.getName();

        BarberDto barber = barberService.getBarberByUsernameOrPhone(usernameOrPhone);
        return ResponseEntity.ok(barber);
    }

    // To look his appointments
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_BARBER')")
    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentDto>> getBarberAppointments(Authentication authentication) {

        String usernameOrPhone = authentication.getName();

        List<AppointmentDto> appointments = barberService.getBarberAppointments(usernameOrPhone);
        return ResponseEntity.ok(appointments);
    }

    // To look his appointments by date
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_BARBER')")
    @GetMapping("/appointments/{date}")
    public ResponseEntity<List<AppointmentDto>> getBarberAppointmentsByDate(
            Authentication authentication,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        String usernameOrPhone = authentication.getName();
        List<AppointmentDto> appointments = barberService.getBarberAppointmentsByDate(usernameOrPhone, date);
        return ResponseEntity.ok(appointments);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_BARBER')")
    @PostMapping("/close-day")
    public ResponseEntity<String> closeDay(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Authentication authentication) {

        String usernameOrPhone = authentication.getName();
        barberService.closeDay(usernameOrPhone,date);

        return ResponseEntity.ok("Date: " + date + " excluded succesfully!");
    }


    /*  ---------------------WITH REGISTER---------------
    @PostMapping
    public ResponseEntity<BarberDto> createBarber(@RequestBody BarberDto barberDto) {
        BarberDto createdBarber = barberService.createBarber(barberDto);
        return new ResponseEntity<>(createdBarber, HttpStatus.CREATED);
    }
     */

}
