package com.example.bbapp.mapper;

import com.example.bbapp.dto.AppointmentDto;
import com.example.bbapp.entity.Appointment;

public class AppointmentMapper {

    public static AppointmentDto toAppointmentDto(Appointment appointment) {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setAppointmentId(appointment.getAppointmentId());
        appointmentDto.setCustomerFirstName(appointment.getCustomer().getFirstName());
        appointmentDto.setCustomerLastName(appointment.getCustomer().getLastName());
        appointmentDto.setCustomerPhone(appointment.getCustomer().getPhone());
        appointmentDto.setBarberName(appointment.getBarber().getBarberName());
        appointmentDto.setAppointmentTime(appointment.getAppointmentTime());
        appointmentDto.setServiceName(appointment.getService().getServiceName());
        appointmentDto.setServicePrice(appointment.getService().getPrice());
        appointmentDto.setStatus(appointment.getStatus());
        appointmentDto.setNotes(appointment.getNotes());

        return appointmentDto;
    }
}
