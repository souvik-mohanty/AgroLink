package com.AgroLink.User.service;

import com.AgroLink.User.dto.UserProfileDTO;
import com.AgroLink.User.dto.UserRequest;
import com.AgroLink.User.enums.UserRole;
import com.AgroLink.User.model.User;
import com.AgroLink.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.function.Consumer;


@Service
@RequiredArgsConstructor // Injects constructor-based dependencies
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //sourav :- transfer from auth ot this :- This service is used to fetch user details by ID.

    public Optional<UserProfileDTO> getUserById(String id) {
        return userRepository.findById(id)
                .map(this::convertToDto);
    }



    public Optional<UserProfileDTO> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::convertToDto);
    }

    // sourav :- This service is used to update user details.
    public Optional<Boolean> updateUser(String id, UserRequest updateUser) {
        try {
            Optional<User> optionalUser = userRepository.findById(id);

            if (optionalUser.isEmpty()) {
                return Optional.empty(); // User not found
            }

            User existingUser = optionalUser.get();

            updateIfPresent(updateUser.getUsername(), existingUser::setUsername);
            updateIfPresent(updateUser.getEmail(), existingUser::setEmail);
            updateIfPresent(updateUser.getAadhar(), existingUser::setAadhar);
            updateIfPresent(updateUser.getContactNumber(), existingUser::setContactNumber);
            updateIfPresent(updateUser.getAddress(), existingUser::setAddress);
            updateIfPresent(updateUser.getPassword(), password -> {
                existingUser.setPassword(passwordEncoder.encode(password));
            });
            updateIfPresent(updateUser.getRole(), role -> {
                try {
                    existingUser.setRole(UserRole.valueOf(role.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid role: " + role); // this should propagate as 400
                }
            });
            updateIfPresent(updateUser.isTwoFactorEnabled(), existingUser::setTwoFactorEnabled);

            userRepository.save(existingUser);
            return Optional.of(true); // success

        } catch (IllegalArgumentException e) {
            throw e; // Let this bubble up to GlobalExceptionHandler
        } catch (Exception e) {
            System.err.println("UserService Update error: " + e.getClass().getSimpleName() + "\n" + e.getMessage());
            throw new RuntimeException("Update failed");
        }
    }


    // sourav :- This service is used to delete user details.
    public Optional<Boolean> deleteUser(String id) {
        try {
            if (!userRepository.existsById(id)) {
                return Optional.empty(); // User not found
            }

            userRepository.deleteById(id);
            return Optional.of(true); // Deleted successfully

        } catch (Exception e) {
            System.err.println("UserService Delete error: " + e.getClass().getSimpleName() + "\n" + e.getMessage());
            throw new RuntimeException("Delete failed");
        }
    }


    private UserProfileDTO convertToDto(User user) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setAadhar(user.getAadhar());
        dto.setRole(user.getRole().toString());
        dto.setContactNumber(user.getContactNumber());
        dto.setAddress(user.getAddress());
        dto.setTwoFactorEnabled(user.isTwoFactorEnabled());
        return dto;
    }


    private void updateIfPresent(String value, Consumer<String> setter) {
        Optional.ofNullable(value)
                .filter(val -> !val.trim().isEmpty())
                .ifPresent(setter);
    }

    private <T> void updateIfPresent(T value, Consumer<T> setter) {
        Optional.ofNullable(value).ifPresent(setter);
    }


}
