package com.example.bbapp.repository;

import com.example.bbapp.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    //If found ok else null(not NullPointerException)
    Optional<Customer> findByPhone(String phone);

}
