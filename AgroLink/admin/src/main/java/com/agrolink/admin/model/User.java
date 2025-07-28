package com.agrolink.admin.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class User {
    @Id
    private String id;
    private String name;
    private String role; // FARMER, ADVISOR, BUYER, ADMIN
    private boolean suspended;
}

