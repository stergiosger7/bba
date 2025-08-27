package com.example.bbapp.repository;

import com.example.bbapp.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service,Long> {
}
