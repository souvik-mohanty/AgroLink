package com.AgroLink.User.service;

import com.AgroLink.User.dto.JwtResponse;
import com.AgroLink.User.dto.LoginRequest;
import com.AgroLink.User.dto.UserRequest;
import com.AgroLink.User.model.User;
import com.AgroLink.User.repository.UserRepository;
import com.AgroLink.User.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authManager;

    public String register(UserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setAadhar(request.getAadhar());
        user.setContactNumber(request.getContactNumber());
        user.setAddress(request.getAddress());
        user.setRoles(request.getRoles());

        userRepository.save(user);
        return "User registered successfully";
    }

    public JwtResponse login(LoginRequest loginRequest) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), loginRequest.getPassword()
                )
        );

        UserDetails user = (UserDetails) auth.getPrincipal();
        String token = jwtUtil.generateToken(user.getUsername(),
                user.getAuthorities().stream().map(authz -> authz.getAuthority()).toList());

        return new JwtResponse(token);
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }
}
