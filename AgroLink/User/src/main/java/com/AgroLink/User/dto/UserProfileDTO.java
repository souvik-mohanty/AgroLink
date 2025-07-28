package com.AgroLink.User.dto;

import lombok.Data;

@Data
public class UserProfileDTO {
    private String id;
    private String username;
    private String email;
    private String aadhar;
    private String role;
    private String contactNumber;
    private String address;
    private boolean twoFactorEnabled;
}
