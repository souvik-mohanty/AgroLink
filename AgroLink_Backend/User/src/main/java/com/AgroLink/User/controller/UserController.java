package com.AgroLink.User.controller;

import com.AgroLink.User.dto.UserProfileDTO;
import com.AgroLink.User.dto.UserRequest;
import com.AgroLink.User.exceptions.UserNotFoundException;
import com.AgroLink.User.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // sourav :- This endpoint is used to get the currently logged-in user's profile.
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileDTO> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return ResponseEntity.ok(userService.getUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username)));
    }

    // sourav :- moved from auth to this :- This endpoint is used to fetch user details by ID.
    @GetMapping("/{id}")
    @PreAuthorize("@authUtil.isUserOrAdmin(#id)")
    public ResponseEntity<UserProfileDTO> getUserById(@PathVariable String id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @GetMapping("/{id}/exists")
    public boolean userExistsAndActive(@PathVariable String id) {
        return userService.userExistsAndActive(id);
    }


    // sourav :- This endpoint is used to update user details.
    @PutMapping("/{id}")
    @PreAuthorize("@authUtil.isUserOrAdmin(#id)")
    public ResponseEntity<String> updateUser(@PathVariable String id, @Valid @RequestBody UserRequest updateUser) {
        return userService.updateUser(id, updateUser)
                .map(success -> ResponseEntity.ok("User updated successfully"))
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    // sourav :- This endpoint is used to delete user details.
    @DeleteMapping("/{id}")
    @PreAuthorize("@authUtil.isUserOrAdmin(#id)")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        return userService.deleteUser(id)
                .map(success -> ResponseEntity.ok("User deleted successfully"))
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
