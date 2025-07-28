package com.AgroLink.User.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "users")
@Data
public class User {
    @Id
    private String id;

    @NotBlank
    private String username;

    private String email;
    private String password;
    private String aadhar;
    private List<String> roles;
    private String contactNumber;
    private String address;
    private boolean twoFactorEnabled;
}

