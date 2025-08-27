package com.example.bbapp.service.impl;

import com.example.bbapp.dto.JwtAuthResponse;
import com.example.bbapp.dto.LoginDto;
import com.example.bbapp.dto.RegisterDto;
import com.example.bbapp.entity.Barber;
import com.example.bbapp.entity.Role;
import com.example.bbapp.exception.BarberAPIException;
import com.example.bbapp.repository.BarberRepository;
import com.example.bbapp.repository.RoleRepository;
import com.example.bbapp.security.JwtJavaProvider;
import com.example.bbapp.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private BarberRepository barberRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtJavaProvider jwtJavaProvider;

    @Override
    public String register(RegisterDto registerDto) {
        //check username is already exists in database
        if(barberRepository.existsByUsernameOrPhone(registerDto.getUsername(), registerDto.getPhone()))
            throw new BarberAPIException(HttpStatus.BAD_REQUEST,"Barber already exists!");

        Barber barber = new Barber();
        barber.setBarberName(registerDto.getName());
        barber.setUsername(registerDto.getUsername());
        barber.setPhone(registerDto.getPhone());
        barber.setPasswordHash(passwordEncoder.encode(registerDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role barberRole = roleRepository.findByRoleName("ROLE_BARBER");
        roles.add(barberRole);

        barber.setRoles(roles);
        barberRepository.save(barber);

        return "Barber registerd successfully!";
    }

    @Override
    public JwtAuthResponse login(LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrPhone(),
                loginDto.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtJavaProvider.generateToken(authentication);

        Optional<Barber> barberOptional = barberRepository.findByUsernameOrPhone(loginDto.getUsernameOrPhone(), loginDto.getUsernameOrPhone());

        String role = null;

        if(barberOptional.isPresent()){
            Barber loggenInBarber = barberOptional.get();
            Optional<Role> optionalRole = loggenInBarber.getRoles().stream().findFirst();

            if(optionalRole.isPresent()){
                Role barberRole = optionalRole.get();
                role = barberRole.getRoleName();
            }
        }

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setRole(role);
        jwtAuthResponse.setAccessToken(token);

        return jwtAuthResponse;
    }
}
