package com.example.bbapp.controller;

import com.example.bbapp.dto.JwtAuthResponse;
import com.example.bbapp.dto.LoginDto;
import com.example.bbapp.dto.RegisterDto;
import com.example.bbapp.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;

    //Build Register REST API
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){

        String response = authService.register(registerDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //Build Login REST API
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto){
        JwtAuthResponse jwtAuthResponse = authService.login(loginDto);
        return new ResponseEntity<>(jwtAuthResponse,HttpStatus.OK);
    }
}
