package com.AgroLink.User.controller;


import com.AgroLink.User.dto.JwtResponse;
import com.AgroLink.User.dto.LoginRequest;
import com.AgroLink.User.dto.UserRequest;
import com.AgroLink.User.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    //sourav :- This endpoint is used to register multiple users at once for dummy data entries.
    @PostMapping("/registers")
    public ResponseEntity<?> registers(@RequestBody List<UserRequest> requests) {
        List<String> registrationResults = new ArrayList<>();

        for (UserRequest request : requests) {
            try {
                String result = authService.register(request);
                registrationResults.add("✅ " + request.getUsername() + ": " + result);
            } catch (Exception e) {
                registrationResults.add("❌ " + request.getUsername() + ": Registration failed - ");
            }
        }

        return ResponseEntity.ok(registrationResults);
    }


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}


