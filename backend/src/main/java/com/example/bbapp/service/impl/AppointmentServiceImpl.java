package com.example.bbapp.service.impl;


import com.example.bbapp.dto.AppointmentDto;
import com.example.bbapp.dto.BarberDto;
import com.example.bbapp.dto.CreateAppointmentDto;
import com.example.bbapp.entity.Appointment;
import com.example.bbapp.entity.AppointmentStatus;
import com.example.bbapp.entity.Barber;
import com.example.bbapp.entity.Customer;
import com.example.bbapp.exception.BarberAPIException;
import com.example.bbapp.mapper.AppointmentMapper;
import com.example.bbapp.mapper.BarberMapper;
import com.example.bbapp.repository.*;
import com.example.bbapp.service.AppointmentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private AppointmentRepository appointmentRepository;
    private BarberRepository barberRepository;
    private CustomerRepository customerRepository;
    private ServiceRepository serviceRepository;
    private final HolidayRepository holidayRepository;

    @Override
    public AppointmentDto createAppointment(CreateAppointmentDto createAppointmentDto) {
        // 1. Validate slot alignment (reject 10:15, 10:45, etc.)
        LocalTime slotTime = createAppointmentDto.getAppointmentTime().toLocalTime();
        if (slotTime.getMinute() % 30 != 0) {  // Only allow :00 or :30
            throw new BarberAPIException(
                    HttpStatus.BAD_REQUEST,
                    "Slots must start at :00 or :30 (e.g., 10:00, 10:30)"
            );
        }

        // 2. Validate business hours (9:00-20:30 as per your code)
        if (slotTime.isBefore(LocalTime.of(9, 0))) {
            throw new BarberAPIException(HttpStatus.BAD_REQUEST, "We open at 9:00 AM");
        }
        if (slotTime.isAfter(LocalTime.of(20, 0))) {  // Last slot ends at 20:30
            throw new BarberAPIException(HttpStatus.BAD_REQUEST, "Last slot at 8:00 PM");
        }

        // Check if slot is available
        if (!isSlotAvailable(createAppointmentDto.getBarberId(), createAppointmentDto.getAppointmentTime())) {
            throw new IllegalStateException("Time slot is not available");
        }

        // Find or create customer
        //If the customer has closed again an appointment I find him by phone and connect him (not to make new customer)
        //Else I create a new customer(id etx)
        Customer customer = customerRepository.findByPhone(createAppointmentDto.getCustomerPhone())
                .orElse(new Customer());

        customer.setFirstName(createAppointmentDto.getCustomerFirstName());
        customer.setLastName(createAppointmentDto.getCustomerLastName());
        customer.setPhone(createAppointmentDto.getCustomerPhone());

        //I want all customer object
        customerRepository.save(customer);

        // Get barber and service objects
        Barber barber = barberRepository.findById(createAppointmentDto.getBarberId())
                .orElseThrow(() -> new EntityNotFoundException("Barber not found"));
        com.example.bbapp.entity.Service service = serviceRepository.findById(createAppointmentDto.getServiceId())
                .orElseThrow(() -> new EntityNotFoundException("Service not found"));

        // Create appointment
        Appointment appointment = new Appointment();

        //Uses objects of barber,customer,service (not only an id for example)
        appointment.setCustomer(customer);
        appointment.setBarber(barber);
        appointment.setService(service);

        appointment.setAppointmentTime(createAppointmentDto.getAppointmentTime());
        appointment.setNotes(createAppointmentDto.getNotes());
        appointment.setStatus(AppointmentStatus.PENDING);

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return AppointmentMapper.toAppointmentDto(savedAppointment);
    }

    @Override
    public AppointmentDto updateAppointment(Long appointmentId, AppointmentDto appointmentDto, String usernameOrPhone) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        Barber barber = barberRepository.findByUsernameOrPhone(usernameOrPhone,usernameOrPhone)
                .orElseThrow(() -> new BarberAPIException(
                        HttpStatus.NOT_FOUND,
                        "Barber not found with username or phone: " + usernameOrPhone
                ));

        if (!appointment.getBarber().getBarberId().equals(barber.getBarberId())) {
            throw new AccessDeniedException("Not your appointment");
        }

        return AppointmentMapper.toAppointmentDto(appointmentRepository.save(appointment));
    }

    @Override
    public void cancelAppointment(Long appointmentId) {
        //delete appointment
        appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment with id: " + appointmentId + " not found"));

        appointmentRepository.deleteById(appointmentId);
    }

    @Override
    public boolean isSlotAvailable(Long barberId, LocalDateTime appointmentTime) {
        return !appointmentRepository.isSlotTaken(barberId, appointmentTime);

    }

    public void confirmAppointment(Long appointmentId, String usernameOrPhone) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        Barber barber = barberRepository.findByUsernameOrPhone(usernameOrPhone,usernameOrPhone)
                .orElseThrow(() -> new BarberAPIException(
                        HttpStatus.NOT_FOUND,
                        "Barber not found with username or phone: " + usernameOrPhone
                ));

        if (!appointment.getBarber().getBarberId().equals(barber.getBarberId())) {
            throw new AccessDeniedException("You are not authorized to confirm this appointment.");
        }

        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointmentRepository.save(appointment);
    }

    @Override
    public List<String> getAvailableDates(Long barberId, YearMonth month) {
        LocalDate today = LocalDate.now();
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();

        return start.datesUntil(end.plusDays(1))
                .filter(date -> !date.isBefore(today))          // Exclude past dates
                .filter(date -> !isWeekend(date))               // Exclude weekends
                .filter(date -> !isHoliday(date))               // Holiday check
                .filter(date -> !isClosedByBarber(date, barberId)) // Barber-specific check
                .filter(date -> !isDateFullyBooked(barberId, date)) // Exclude full days
                .map(LocalDate::toString)                       // ISO format
                .collect(Collectors.toList());
    }

    private boolean isClosedByBarber(LocalDate date, Long barberId) {
        return holidayRepository.existsByMonthAndDayAndBarberId(
                date.getMonthValue(),
                date.getDayOfMonth(),
                barberId
        );
    }

    private boolean isHoliday(LocalDate date) {
        return holidayRepository.existsByMonthAndDay(date.getMonthValue(), date.getDayOfMonth());
    }

    private boolean isWeekend(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

    private boolean isDateFullyBooked(Long barberId, LocalDate date) {
        //int totalSlots = numOfSlotsDaily();
        //int totalSlots = 20; // 9:00-19:00 in 30-min slots
        return appointmentRepository.countAppointmentsByBarberIdAndDate(barberId, date) >= numOfSlotsDaily();
    }


    public int numOfSlotsDaily(){
        // All available slots between 9:00-19:00
        int numOfSlots = 0;

        List<LocalTime> allSlots = new ArrayList<>();
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(19, 30);

        while (start.isBefore(end)) {
            allSlots.add(start);
            start = start.plusMinutes(30);
            numOfSlots+=1;
        }

        //System.out.println(numOfSlots);
        return numOfSlots;
    }

    @Override
    public List<LocalTime> getAvailableSlots(Long barberId, LocalDate date) {
        // All available slots between 9:00-19:00
        List<LocalTime> allSlots = new ArrayList<>();
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(19, 30);

        while (start.isBefore(end)) {
            allSlots.add(start);
            start = start.plusMinutes(30);
        }

        if (!barberRepository.existsById(barberId)) {
            throw new BarberAPIException(HttpStatus.NOT_FOUND, "Barber not found!");
        }

        // All barbers taken appointments
        List<Appointment> takenAppointments = appointmentRepository.findAppointmentsByExactDate(date, barberId);

        Set<LocalTime> takenTimes = takenAppointments.stream()
                .map((appointment) -> appointment.getAppointmentTime().toLocalTime())
                .collect(Collectors.toSet());

        // Return available slots
        return allSlots.stream()
                .filter(slot -> !takenTimes.contains(slot))
                .collect(Collectors.toList());
    }
}
