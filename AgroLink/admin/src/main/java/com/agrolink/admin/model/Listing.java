package com.agrolink.admin.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class Listing {
    @Id
    private String id;
    private String cropName;
    private String userId;
    private boolean approved;
}

