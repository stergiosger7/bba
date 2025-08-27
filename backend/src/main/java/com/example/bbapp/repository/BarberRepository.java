package com.example.bbapp.repository;

import com.example.bbapp.entity.Barber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BarberRepository extends JpaRepository<Barber, Long> {

    Optional<Barber> findByUsername(String username);
    Optional<Barber> findByUsernameOrPhone(String username, String phone);

    Boolean existsByUsernameOrPhone(String username,String phone);
}
