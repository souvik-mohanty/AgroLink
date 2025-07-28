package com.AgroLink.User.service;

import com.AgroLink.User.dto.JwtResponse;
import com.AgroLink.User.dto.LoginRequest;
import com.AgroLink.User.dto.UserRequest;
import com.AgroLink.User.enums.UserRole;
import com.AgroLink.User.model.User;
import com.AgroLink.User.repository.UserRepository;
import com.AgroLink.User.security.JwtUtil;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authManager;


    // sourav :- This service is used to register a new user role.
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
        user.setTwoFactorEnabled(request.isTwoFactorEnabled());

        try {
            user.setRole(UserRole.valueOf(request.getRole().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + request.getRole());
        }

        userRepository.save(user);
        return "User registered successfully";
    }


    // sourav :- Update login for Bad Credentials Exception.
    public JwtResponse login(LoginRequest loginRequest) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            UserDetails user = (UserDetails) auth.getPrincipal();


            String role = user.getAuthorities()
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("User has no role assigned"))
                    .getAuthority()
                    .replaceAll("[\\[\\]]", "");; // returns the role as a string like "ROLE_ADMIN"


            String token = jwtUtil.generateToken(user.getUsername(), role);

            Date expiration = Jwts.parserBuilder()
                    .setSigningKey(jwtUtil.getKey()) // expose `getKey()` in JwtUtil if needed
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();

            return JwtResponse
                    .builder()
                    .role(user.getAuthorities().toString())
                    .token(token)
                    .expiresAt(expiration.toString())
                    .build();

        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Invalid username or password");
        }

    }

    // sourav :- transferred a function
}
