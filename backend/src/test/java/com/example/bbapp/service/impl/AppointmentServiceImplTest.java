package com.example.bbapp.service.impl;

import com.example.bbapp.dto.AppointmentDto;
import com.example.bbapp.dto.CreateAppointmentDto;
import com.example.bbapp.entity.*;
import com.example.bbapp.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private BarberRepository barberRepository;

    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private int currentYear;
    private YearMonth currentYearMonth;
    private Barber testBarber;
    private Service testService;
    private Customer testCustomer;
    private Appointment appointment;
    private CreateAppointmentDto validAppointmentDto;

    @BeforeEach
    void setUp() {
        // Setup runs before each test method
        // This is where we prepare the test data that will be used in multiple tests

        currentYear = Year.now().getValue(); // Get current year dynamically
        currentYearMonth = YearMonth.now(); // Get current year-month dynamically

        testBarber = new Barber();
        testBarber.setBarberId(1L);
        testBarber.setBarberName("Dimitris");
        testBarber.setPhone("6999999999");
        testBarber.setUsername("Dimitrisbarber");

        testService = new Service();
        testService.setServiceId(1L);
        testService.setServiceName("Haircut");
        testService.setPrice(BigDecimal.valueOf(10.0));

        testCustomer = new Customer();
        testCustomer.setCustomerId(1L);
        testCustomer.setFirstName("Stergios");
        testCustomer.setLastName("Gerasis");
        testCustomer.setPhone("6945842316");

        appointment = new Appointment();
        appointment.setAppointmentId(1L);
        appointment.setBarber(testBarber);
        appointment.setService(testService);
        appointment.setStatus(AppointmentStatus.PENDING);

        validAppointmentDto = new CreateAppointmentDto();
        validAppointmentDto.setBarberId(1L);
        validAppointmentDto.setServiceId(1L);
        validAppointmentDto.setCustomerFirstName("Stergios");
        validAppointmentDto.setCustomerLastName("Gerasis");
        validAppointmentDto.setCustomerPhone("6945842316");
        validAppointmentDto.setAppointmentTime(LocalDateTime.of(currentYear, 8, 27, 10, 0));
    }

    @Test
    void createAppointmentSuccesfully() {
        //when
        when(customerRepository.findByPhone("6945842316")).thenReturn(Optional.of(testCustomer));
        when(barberRepository.findById(1L)).thenReturn(Optional.of(testBarber));
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(testService));
        when(appointmentRepository.isSlotTaken(1L, validAppointmentDto.getAppointmentTime())).thenReturn(false);

        Appointment savedAppointment = new Appointment();
        savedAppointment.setAppointmentId(1L);
        savedAppointment.setCustomer(testCustomer);
        savedAppointment.setBarber(testBarber);
        savedAppointment.setService(testService);
        savedAppointment.setAppointmentTime(validAppointmentDto.getAppointmentTime());
        savedAppointment.setStatus(AppointmentStatus.PENDING);

        when(appointmentRepository.save(ArgumentMatchers.any(Appointment.class))).thenReturn(savedAppointment);

        // act
        AppointmentDto result = appointmentService.createAppointment(validAppointmentDto);

        // assert
        assertNotNull(result);
        assertEquals(1L, result.getAppointmentId());
        verify(customerRepository, times(1)).save(testCustomer);
        verify(appointmentRepository, times(1)).save(ArgumentMatchers.any(Appointment.class));
    }

    @Test
    void cancelAppointmentSuccesfully() {
        //given
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        //when
        appointmentService.cancelAppointment(1L);

        //then
        verify(appointmentRepository, times(1)).deleteById(1L);
    }

    @Test
    void confirmAppointmentSuccesfully() {
        //given
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(barberRepository.findByUsernameOrPhone("Dimitrisbarber", "Dimitrisbarber")).thenReturn(Optional.of(testBarber));

        //when
        appointmentService.confirmAppointment(1L,"Dimitrisbarber");

        //then
        assertEquals(AppointmentStatus.CONFIRMED, appointment.getStatus());
        verify(appointmentRepository, times(1)).save(appointment);

    }


    @Test
    void isSlotAvailableReturnTrue() {
        //given
        when(appointmentRepository.isSlotTaken(1L, LocalDateTime.of(currentYear,8,27,10,0))).thenReturn(false);

        //when
        boolean result = appointmentService.isSlotAvailable(1L,LocalDateTime.of(currentYear,8,27,10,0));

        //then
        assertTrue(result);
    }

    @Test
    void isSlotNotAvailableShouldThrowException() {
        //given
        when(appointmentRepository.isSlotTaken(1L,validAppointmentDto.getAppointmentTime())).thenReturn(true);

        //when and then
        IllegalStateException exception = assertThrows(IllegalStateException.class, ()-> appointmentService.createAppointment(validAppointmentDto));

        assertEquals("Time slot is not available", exception.getMessage());
    }

    @Test
    void getAvailableSlotsInValidDateShouldReturnAvailableSlots() {
        //given
        LocalDate date = LocalDate.of(currentYear,8,27);
        when(barberRepository.existsById(1L)).thenReturn(true);

        List<Appointment> takenAppointments = new ArrayList<>();
        appointment.setAppointmentTime(LocalDateTime.of(date, LocalTime.of(10,0) ));
        takenAppointments.add(appointment);

        when(appointmentRepository.findAppointmentsByExactDate(date,1l)).thenReturn(takenAppointments);

        //when
        List<LocalTime> result = appointmentService.getAvailableSlots(1L,date);

        //then
        assertNotNull(result);
        assertFalse(result.contains(LocalTime.of(10,0))); //taken should be excluded
        assertTrue(result.contains(LocalTime.of(9,0))); // An available slot
    }
}