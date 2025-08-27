package com.example.bbapp.repository;

import com.example.bbapp.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment ,Long> {

    //Find all appointments of a barber by barberId
    @Query("SELECT a FROM Appointment a WHERE a.barber.barberId = :barberId")
    List<Appointment> findAppointmentsByBarberId(@Param("barberId") Long barberId);


    //Find all appointments by date
    @Query("SELECT a FROM Appointment a WHERE FUNCTION('DATE', a.appointmentTime) = :date AND a.barber.barberId = :barberId")
    List<Appointment> findAppointmentsByExactDate(@Param("date") LocalDate date, @Param("barberId") Long barberId);

    //Find taken slots
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
            "FROM Appointment a " +
            "WHERE a.barber.id = :barberId AND a.appointmentTime = :appointmentTime")
    boolean isSlotTaken(@Param("barberId") Long barberId, @Param("appointmentTime") LocalDateTime appointmentTime);

    @Query("SELECT COUNT(a) FROM Appointment a WHERE " +
            "a.barber.id = :barberId AND " +
            "CAST(a.appointmentTime AS date) = :date")
    int countAppointmentsByBarberIdAndDate(@Param("barberId") Long barberId,
                               @Param("date") LocalDate date);

}
