package com.AgroLink.User.dto;

import lombok.Data;

@Data
public class UserRequest {
    private String username;
    private String password;
    private String email;
    private String aadhar;
    private String contactNumber;
    private String address;
    private String role;
    private boolean twoFactorEnabled;
}

