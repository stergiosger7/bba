package com.example.bbapp.servicesInitializer;

import com.example.bbapp.entity.Service;
import com.example.bbapp.repository.ServiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@AllArgsConstructor
public class ServiceInitializer  implements CommandLineRunner {

    private final ServiceRepository serviceRepository;

    @Override
    public void run(String... args) throws Exception {
        if (serviceRepository.count() == 0) {
            serviceRepository.save(new Service(null, "Λούσιμο", new BigDecimal("5.00"), 30));
            serviceRepository.save(new Service(null, "Κούρεμα", new BigDecimal("10.00"), 30));
            serviceRepository.save(new Service(null, "Γένια", new BigDecimal("5.00"), 30));
        }else {
            //The services has already constructed
            System.out.println("Services already present");
        }
    }

    // by default admin-barber
}
