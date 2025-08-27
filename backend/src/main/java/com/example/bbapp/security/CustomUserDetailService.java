package com.example.bbapp.security;

import com.example.bbapp.entity.Barber;
import com.example.bbapp.repository.BarberRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private BarberRepository barberRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrPhone) throws UsernameNotFoundException {

        Barber barber =  barberRepository.findByUsernameOrPhone(usernameOrPhone, usernameOrPhone).
                orElseThrow(() -> new UsernameNotFoundException("Barber not exists by username or phone"));

        Set<GrantedAuthority> authorities = barber.getRoles().stream()
                .map((role) -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toSet());

        return new User(usernameOrPhone,barber.getPasswordHash(),authorities);
    }
}
