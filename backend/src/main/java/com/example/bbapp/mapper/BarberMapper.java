package com.example.bbapp.mapper;

import com.example.bbapp.dto.BarberDto;
import com.example.bbapp.entity.Barber;
import com.example.bbapp.entity.Role;

import java.util.Set;
import java.util.stream.Collectors;


public class BarberMapper {

    public static BarberDto toBarberDto(Barber barber){
        BarberDto barberDto = new BarberDto();
        barberDto.setBarberId(barber.getBarberId());
        barberDto.setBarberName(barber.getBarberName());
        barberDto.setPhone(barber.getPhone());
        barberDto.setUsername(barber.getUsername());
        barberDto.setCreatedAt(barber.getCreatedAt());

        if (barber.getRoles() != null) {
            Set<String> roleNames = barber.getRoles().stream()
                    .map(Role::getRoleName)
                    .collect(Collectors.toSet());
            barberDto.setRoles(roleNames);
        }

        return barberDto;
    }

    public static Barber toBarberEntity(BarberDto dto) {
        Barber barber = new Barber();
        barber.setBarberId(dto.getBarberId());
        barber.setBarberName(dto.getBarberName());
        barber.setPhone(dto.getPhone());
        barber.setUsername(dto.getUsername());
        barber.setCreatedAt(dto.getCreatedAt());

        return barber;
    }
}
