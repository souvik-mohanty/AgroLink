package com.AgroLink.User.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserRequest {
    private String username;
    private String password;
    private String email;
    private String aadhar;
    private String contactNumber;
    private String address;
    private List<String> roles;
    private boolean twoFactorEnabled;
}

