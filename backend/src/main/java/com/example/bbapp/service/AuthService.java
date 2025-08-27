package com.example.bbapp.service;

import com.example.bbapp.dto.JwtAuthResponse;
import com.example.bbapp.dto.LoginDto;
import com.example.bbapp.dto.RegisterDto;

public interface AuthService {
    String register(RegisterDto registerDto);

    JwtAuthResponse login(LoginDto loginDto);
}
