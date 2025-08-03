package com.AgroLink.User.security;

import com.AgroLink.User.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

//sourav :- verify a requesting is the user or the admin not other
@Component("authUtil")
public class AuthUtil {

    private final UserRepository userRepository;

    public AuthUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Checks if the authenticated user is the owner of the resource or an admin.
     * This is a more efficient approach that avoids an unnecessary database call.
     * @param requestedUserId The ID of the user being requested.
     * @return true if the current user is an admin or the owner of the resource, false otherwise.
     */
    public boolean isUserOrAdmin(String requestedUserId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }

        // Check if the authenticated user has the 'ROLE_ADMIN' authority
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        // If the user is an admin, they can access any resource
        if (isAdmin) {
            return true;
        }

        // Check if the authenticated user is the owner of the requested resource
        String currentUserId = auth.getName();
        return currentUserId.equals(requestedUserId);
    }
}
