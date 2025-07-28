package com.AgroLink.User.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder
public class JwtResponse {
    private String token;
    private String role;
    private String expiresAt;
}
