package com.example.bbapp.servicesInitializer;

import com.example.bbapp.entity.Barber;
import com.example.bbapp.entity.Role;
import com.example.bbapp.repository.BarberRepository;
import com.example.bbapp.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Getter
@Setter
@AllArgsConstructor
@Transactional
public class AdminBarberInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final BarberRepository barberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // Check and create roles if they don't exist
        Role adminRole = roleRepository.findByRoleName("ROLE_ADMIN");
        if (adminRole == null) {
            adminRole = new Role();
            adminRole.setRoleName("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }

        Role barberRole = roleRepository.findByRoleName("ROLE_BARBER");
        if (barberRole == null) {
            barberRole = new Role();
            barberRole.setRoleName("ROLE_BARBER");
            roleRepository.save(barberRole);
        }

        // Create admin barber if doesn't exist
        if (!barberRepository.existsByUsernameOrPhone("admin","6945842316")) {
            Barber admin = new Barber();
            admin.setUsername("admin");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setBarberName("Stergios Barber");
            admin.setPhone("6945842316");
            admin.setRoles(Set.of(adminRole, barberRole));

            barberRepository.save(admin);
            System.out.println("✅ Default admin barber created");
        } else {
            System.out.println("ℹ️ Admin barber already exists");
        }
    }

}
