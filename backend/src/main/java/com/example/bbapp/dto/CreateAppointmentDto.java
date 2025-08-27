package com.example.bbapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAppointmentDto {

    @NotNull
    private Long barberId;

    //@NotBlank
    private String customerFirstName;

    private String customerLastName;

    //@NotBlank na valo exceptions
    private String customerPhone;

    @NotNull
    private LocalDateTime appointmentTime;

    @NotNull
    private Long serviceId;

    private String notes;
}
