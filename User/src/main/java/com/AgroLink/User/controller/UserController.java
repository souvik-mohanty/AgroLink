package com.AgroLink.User.controller;

import com.AgroLink.User.model.User;
import com.AgroLink.User.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FARMER','BUYER','CARRIER','MANAGER','WAREHOUSE_OPERATOR','ADVISOR')")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        return authService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

