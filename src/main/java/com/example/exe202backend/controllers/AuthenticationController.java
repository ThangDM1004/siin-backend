package com.example.exe202backend.controllers;

import com.example.exe202backend.dto.CreateUserDTO;
import com.example.exe202backend.request.AuthenticationRequest;
import com.example.exe202backend.response.ResponseObject;
import com.example.exe202backend.services.Auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    @Autowired
    private AuthService service;

    @PostMapping("/register")
    public ResponseEntity<ResponseObject> register(@RequestBody CreateUserDTO request) {
        return  service.register(request);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ResponseObject> authenticate(@RequestBody AuthenticationRequest request) {
        return service.authenticate(request);
    }
    @PostMapping("/logout")
    public ResponseEntity<ResponseObject> logout(@RequestParam("token") String token){
        return service.logout(token);
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<ResponseObject> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return service.refreshToken(request,response);
    }
}
