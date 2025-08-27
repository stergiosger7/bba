package com.example.bbapp.mapper;

import com.example.bbapp.dto.AppointmentDto;
import com.example.bbapp.entity.Appointment;

public class AppointmentMapper {

    public static AppointmentDto toAppointmentDto(Appointment appointment) {
        AppointmentDto barberDto = new AppointmentDto();
        barberDto.setAppointmentId(appointment.getAppointmentId());
        barberDto.setCustomerFirstName(appointment.getCustomer().getFirstName());
        barberDto.setCustomerLastName(appointment.getCustomer().getLastName());
        barberDto.setCustomerPhone(appointment.getCustomer().getPhone());
        barberDto.setBarberName(appointment.getBarber().getBarberName());
        barberDto.setAppointmentTime(appointment.getAppointmentTime());
        barberDto.setServiceName(appointment.getService().getServiceName());
        barberDto.setServicePrice(appointment.getService().getPrice());
        barberDto.setStatus(appointment.getStatus());
        barberDto.setNotes(appointment.getNotes());

        return barberDto;
    }
}
