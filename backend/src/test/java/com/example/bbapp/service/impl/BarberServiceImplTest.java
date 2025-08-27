package com.example.bbapp.service.impl;

import com.example.bbapp.dto.AppointmentDto;
import com.example.bbapp.dto.BarberDto;
import com.example.bbapp.dto.CreateAppointmentDto;
import com.example.bbapp.entity.Appointment;
import com.example.bbapp.entity.Barber;
import com.example.bbapp.entity.Customer;
import com.example.bbapp.entity.Service;
import com.example.bbapp.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BarberServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private BarberRepository barberRepository;

    @InjectMocks
    private BarberServiceImpl barberService;

    private Barber testBarber;
    private Barber testBarber2;
    private Appointment appointment1;
    private Appointment appointment2;
    private Customer testCustomer;
    private Service testService;



    @BeforeEach
    void setUp() {
        LocalDateTime dateTime1 = LocalDateTime.of(2025, 8, 27,10,0);
        LocalDateTime dateTime2 = LocalDateTime.of(2025, 8, 27,10,30);

        testBarber = new Barber();
        testBarber.setBarberId(1L);
        testBarber.setBarberName("Dimitris");
        testBarber.setPhone("6999999999");
        testBarber.setUsername("Dimitrisbarber");

        testService = new Service();
        testService.setServiceId(1L);
        testService.setServiceName("Haircut");
        testService.setPrice(BigDecimal.valueOf(10.0));

        testBarber2 = new Barber();
        testBarber2.setBarberId(2L);
        testBarber2.setBarberName("Orestis");
        testBarber2.setPhone("6999999910");
        testBarber2.setUsername("Orestisbarber");

        testCustomer = new Customer();
        testCustomer.setCustomerId(1L);
        testCustomer.setFirstName("Stergios");
        testCustomer.setLastName("Gerasis");
        testCustomer.setPhone("6945842316");

        appointment1 = new Appointment();
        appointment1.setAppointmentId(1L);
        appointment1.setAppointmentTime(dateTime1);
        appointment1.setBarber(testBarber);
        appointment1.setCustomer(testCustomer);
        appointment1.setService(testService);

        appointment2 = new Appointment();
        appointment2.setAppointmentId(2L);
        appointment2.setAppointmentTime(dateTime2);
        appointment2.setBarber(testBarber);
        appointment2.setCustomer(testCustomer);
        appointment2.setService(testService);
    }

    @Test
    void getAllBarbersSuccesfully() {
        //given
        List<Barber> barbers = new ArrayList<>();
        barbers.add(testBarber);
        barbers.add(testBarber2);

        when(barberRepository.findAll()).thenReturn(barbers);

        //when
        List<BarberDto> result = barberService.getAllBarbers();

        // assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Dimitris", result.get(0).getBarberName());
        assertEquals("Orestis", result.get(1).getBarberName());
    }

    @Test
    void getBarberByUsernameOrPhone() {
        //given
        when(barberRepository.findByUsernameOrPhone("Dimitrisbarber","Dimitrisbarber")).thenReturn(Optional.of(testBarber));

        //when
        BarberDto result = barberService.getBarberByUsernameOrPhone("Dimitrisbarber");

        //then
        assertNotNull(result);
        assertEquals("Dimitris", testBarber.getBarberName());
        assertEquals("6999999999", testBarber.getPhone());

    }

    @Test
    void deleteBarberSuccesfully() {
        //given
        testBarber.setRoles(new HashSet<>());

        when(barberRepository.findById(1l)).thenReturn(Optional.of(testBarber));

        //when
        barberService.deleteBarber(1l);

        //then
        verify(barberRepository,times(1)).findById(1L);
        verify(barberRepository,times(1)).save(testBarber);
        verify(barberRepository,times(1)).delete(testBarber);

        assertTrue(testBarber.getRoles().isEmpty());

    }

    @Test
    void getBarberAppointmentsByDateSuccesfully() {
        //given
        LocalDate date = LocalDate.of(2025,8,27);

        List<Appointment> appointments = List.of(appointment1,appointment2);

        when(barberRepository.findByUsernameOrPhone("Dimitrisbarber","Dimitrisbarber")).thenReturn(Optional.of(testBarber));
        when(appointmentRepository.findAppointmentsByExactDate(date,1L)).thenReturn(appointments);

        //when
        List<AppointmentDto> result = barberService.getBarberAppointmentsByDate("Dimitrisbarber",date);

        //then
        assertNotNull(result);
        assertEquals(2,result.size());
        assertEquals(1L, result.get(0).getAppointmentId());
        assertEquals(2L, result.get(1).getAppointmentId());

    }
}